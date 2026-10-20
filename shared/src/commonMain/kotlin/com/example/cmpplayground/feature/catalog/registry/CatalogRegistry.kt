package com.example.cmpplayground.feature.catalog.registry

import com.example.cmpplayground.feature.catalog.model.CatalogCategory
import com.example.cmpplayground.feature.catalog.model.CatalogDemo
import com.example.cmpplayground.feature.catalog.model.CatalogEntry

object CatalogRegistry {
    private val categoryNames = linkedMapOf(
        "essentials" to "Essentials",
        "layout" to "Layout & containers",
        "input" to "Forms & input",
        "feedback" to "Feedback & overlays",
        "motion" to "Motion",
        "advanced" to "Advanced & platform",
    )
    val categories = categoryNames.map { (id, title) ->
        CatalogCategory(id, title, CatalogDemo.entries.filter { it.categoryId == id }.map {
            CatalogEntry(it.id, it.title, id)
        })
    }
    val allEntries = categories.flatMap { it.demos }

    fun search(query: String): List<CatalogEntry> {
        val terms = query.trim().split(Regex("\\s+")).filter(String::isNotEmpty)
        return allEntries.filter { entry ->
            val content = "${entry.title} ${categoryNames.getValue(entry.categoryId)}"
            terms.all { content.contains(it, ignoreCase = true) }
        }
    }
}
