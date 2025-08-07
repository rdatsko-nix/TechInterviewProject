import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val logger = CounterLogger()

    val jobs = List(5) {
        launch(Dispatchers.Default) {
            repeat(50) {
                logger.increment()
                delay(100)
            }
        }
    }

    jobs.joinAll()

    println("Expected size: ${5 * 50}, actual: ${logger.logs.size}")
    println("Max value in logs: ${logger.logs.maxOrNull()}")
    println("Logs: ${logger.logs}")
}

