package org.grails.plugin.resource

import javax.servlet.*
import org.springframework.web.context.support.WebApplicationContextUtils
import grails.util.Environment

/**
 * This just traps any obvious mistakes the user has made and warns them in dev mode
 * 
 * @author Marc Palmer (marc@grailsrocks.com)
 */
class DevModeSanityFilter implements Filter {
    def grailsResourceProcessor
    
    void init(FilterConfig config) throws ServletException {
        def applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.servletContext)
        grailsResourceProcessor = applicationContext.grailsResourceProcessor
    }

    void destroy() {
    }

    void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, response)

        if (request.getAttribute('resources.need.layout')) {
            def dispositionsLeftOver = grailsResourceProcessor.getRequestDispositionsRemaining(request)
            if (dispositionsLeftOver) {
                def optionals = grailsResourceProcessor.optionalDispositions
                dispositionsLeftOver -= optionals
                if (dispositionsLeftOver) {
                    throw new RuntimeException("It looks like you are missing some calls to the r:layoutResources tag. "+
                        "After rendering your page the following have not been rendered: ${dispositionsLeftOver}")
                }
            }
        }

    }
}