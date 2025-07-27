package com.steptrace.steptrace.support

import com.steptrace.steptrace.config.security.CustomUserDetails
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Component
class AuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated || authentication.principal == "anonymousUser") {
            return Optional.empty()
        }

        val customUserDetails = authentication.principal as CustomUserDetails
        return Optional.of(customUserDetails.getId().toString())
    }
}
