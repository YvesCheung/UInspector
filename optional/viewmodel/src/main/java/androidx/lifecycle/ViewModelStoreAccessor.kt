package androidx.lifecycle

/**
 * @author YvesCheung
 * 2021/9/30
 */
object ViewModelStoreAccessor {

    fun get(store: ViewModelStore, key: String): ViewModel {
        return store.get(key)
    }

    fun keys(store: ViewModelStore): Set<String> {
        return store.keys()
    }
}