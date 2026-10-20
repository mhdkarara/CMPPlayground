package com.example.cmpplayground

import com.example.cmpplayground.feature.catalog.examples.EssentialsCodeExamples
import com.example.cmpplayground.feature.catalog.model.CatalogDemo
import com.example.cmpplayground.feature.catalog.registry.CatalogRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CatalogTest {
    @Test
    fun lessonsHaveUniqueStableIdsAndValidCategories() {
        assertEquals(listOf("essentials", "layout", "input", "feedback", "motion", "advanced"),
            CatalogRegistry.categories.map { it.id })
        assertEquals(43, CatalogRegistry.allEntries.size)
        assertEquals(CatalogDemo.entries.map { it.id }.toSet(), CatalogRegistry.allEntries.map { it.id }.toSet())
        assertEquals(CatalogRegistry.allEntries.size, CatalogRegistry.allEntries.map { it.id }.distinct().size)
        CatalogRegistry.categories.forEach { category ->
            assertTrue(category.demos.isNotEmpty())
            category.demos.forEach { entry ->
                val lesson = assertNotNull(CatalogDemo.fromId(entry.id))
                assertEquals(category.id, lesson.categoryId)
                assertEquals(category.id, entry.categoryId)
                assertEquals(lesson.title, entry.title)
                assertTrue(lesson.goal.isNotBlank())
                assertTrue(Regex("[a-z]+(?:-[a-z]+)*").matches(lesson.id))
            }
        }
        assertNull(CatalogDemo.fromId("unknown-lesson"))
    }

    @Test
    fun searchMatchesConceptsScenariosAndCategories() {
        assertEquals(listOf(CatalogDemo.WeatherCrossfade.id), CatalogRegistry.search(" cRoSsFaDe ").map { it.id })
        assertEquals(10, CatalogRegistry.search("essentials").size)
        assertEquals(listOf(CatalogDemo.PaceChoice.id), CatalogRegistry.search("forms radio pace").map { it.id })
        assertEquals(listOf(CatalogDemo.SaveActions.id), CatalogRegistry.search("buttons save route").map { it.id })
        assertTrue(CatalogRegistry.search("not-a-trail-lesson").isEmpty())
        assertEquals(CatalogRegistry.allEntries, CatalogRegistry.search("  "))
    }

    @Test
    fun discoverAndLearnDestinationsResolveToTheExpectedCategory() {
        val shortcuts = mapOf(
            "save-actions" to "essentials", "reading-type" to "essentials",
            "gear-row" to "layout", "trip-notes" to "input", "native-controls" to "advanced",
        )
        shortcuts.forEach { (id, category) ->
            assertEquals(category, CatalogRegistry.allEntries.single { it.id == id }.categoryId)
        }
    }

    @Test
    fun everyEssentialsLessonHasBothCodeComparisonsAndAnExplanation() {
        val ids = CatalogRegistry.categories.first { it.id == "essentials" }.demos.map { it.id }.toSet()
        assertEquals(ids, EssentialsCodeExamples.byDemoId.keys)
        EssentialsCodeExamples.byDemoId.values.forEach {
            assertTrue(it.swiftCode.isNotBlank())
            assertTrue(it.kotlinCode.isNotBlank())
            assertTrue(it.explanation.isNotBlank())
            assertTrue(it.keyDifferences.isNotEmpty())
        }
        val image = EssentialsCodeExamples.byDemoId.getValue(CatalogDemo.TrailBadge.id)
        assertTrue(image.kotlinCode.contains("Res.drawable.trail_marker"))
        assertTrue(image.swiftCode.contains("trail_marker"))
    }
}
