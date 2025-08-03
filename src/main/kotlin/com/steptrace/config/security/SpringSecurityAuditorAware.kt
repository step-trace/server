package com.steptrace.config.security

import org.springframework.stereotype.Component
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

@Component
class SpringSecurityAuditorAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated || authentication.principal == "anonymousUser") {
            return Optional.of("SYSTEM")
        }

        val auditor = if (authentication.principal is CustomUserDetails) {
            (authentication.principal as CustomUserDetails).username
        } else {
            authentication.principal.toString()
        }

        return Optional.of(auditor)
    }
}
