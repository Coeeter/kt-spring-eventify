package com.nasportfolio.eventify.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class EventifyPageRequest(
    val page: Int,
    val size: Int,
    sort: Sort = Sort.unsorted()
) : PageRequest(validatePage(page), validateSize(size), sort) {
    companion object {
        private fun validatePage(page: Int): Int {
            if (page < 0) {
                throw InvalidPageException("Invalid page or size given")
            }
            return page
        }

        private fun validateSize(size: Int): Int {
            if (size < 1) {
                throw InvalidPageException("Invalid page or size given")
            }
            return size
        }
    }
}