package io.synthesized.kotlin

import io.synthesized.DataTransfer
import kotlinx.coroutines.*

suspend fun main() {
    withContext(Dispatchers.IO) {
        val deps = DataTransfer("departments", listOf("department_name"), scope = this)
        val projs = DataTransfer("projects", listOf("project_name"), parents = listOf(deps))
        val emps = DataTransfer("employees", listOf("first_name", "last_name", "email"),
            parents = listOf(deps))
        val empProjs = DataTransfer("employee_projects", emptyList(),
            parents = listOf(emps, projs))

        launch(CoroutineName("employee_projects")) {
            empProjs.run()
        }
    }
}