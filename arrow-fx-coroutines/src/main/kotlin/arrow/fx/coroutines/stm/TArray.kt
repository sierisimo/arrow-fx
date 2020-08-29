package arrow.fx.coroutines.stm

import arrow.fx.coroutines.STM

suspend fun <A> STM.newTArray(size: Int, f: (Int) -> A): TArray<A> =
  TArray(Array(size) { i -> TVar(f(i)) })
suspend fun <A> STM.newTArray(size: Int, a: A): TArray<A> =
  newTArray(size) { a }
suspend fun <A> STM.newTArray(vararg arr: A): TArray<A> =
  TArray(arr.map { newTVar(it) }.toTypedArray())
suspend fun <A> STM.newTArray(xs: Iterable<A>): TArray<A> =
  TArray(xs.map { newTVar(it) }.toTypedArray())

/**
 * A [TArray] is an array of transactional variables.
 */
data class TArray<A>internal constructor(internal val v: Array<TVar<A>>) {

  fun size(): Int = v.size

  override fun equals(other: Any?): Boolean = this === other
  override fun hashCode(): Int = v.hashCode()

  companion object {
    suspend fun <A> new(size: Int, f: (Int) -> A): TArray<A> =
      TArray(Array(size) { i -> TVar(f(i)) })
    suspend fun <A> new(size: Int, a: A): TArray<A> =
      new(size) { a }
    suspend fun <A> new(vararg arr: A): TArray<A> =
      TArray(arr.map { TVar.new(it) }.toTypedArray())
    suspend fun <A> new(xs: Iterable<A>): TArray<A> =
      TArray(xs.map { TVar.new(it) }.toTypedArray())
  }
}
