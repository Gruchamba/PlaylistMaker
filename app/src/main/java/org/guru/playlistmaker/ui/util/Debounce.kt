package org.guru.playlistmaker.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Каждый вызов этой новой функции будет  отложен на указанное количество времени и
 * только последний вызов будет выполнен, если useLastParam равен true.
 * Если useLastParam равен false, то первый вызов будет отложен на delayMills, и пока он
 * не выполнится, все последующие вызовы будут игнорироваться.
 */
fun <T> debounce(delayMillis: Long,
                 coroutineScope: CoroutineScope,
                 useLastParam: Boolean,
                 action: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}