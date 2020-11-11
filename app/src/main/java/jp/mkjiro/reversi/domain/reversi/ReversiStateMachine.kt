package jp.mkjiro.reversi.domain.reversi

abstract class ReversiStateMachine {
    protected lateinit var state: State

    protected abstract fun onInit()
    protected abstract fun onStart()
    protected abstract fun onJudgeFirst()
    protected abstract fun onJudgeSecond()
    protected abstract fun onTurnPlayer()

    protected fun emitEvent() {
    }

    protected fun changeState(state: State) {
        this.state = state
        onStateChanged()
    }

    private fun onStateChanged() {
        when (state) {
            State.INIT -> {
                onInit()
            }
            State.START -> {
                onStart()
            }
            State.JUDGE_FIRST -> {
                onJudgeFirst()
            }
            State.JUDGE_SECOND -> {
                onJudgeSecond()
            }
            State.PLAYER -> {
                onTurnPlayer()
            }
            State.PROCESSING -> {
            }
            State.FINISH -> {
            }
            else -> {
            }
        }
    }

    enum class State {
        INIT,
        START,
        JUDGE_FIRST,
        JUDGE_SECOND,
        PROCESSING,
        PLAYER,
        TURN_OF_HUMAN,
        TURN_OF_CPU,
        FINISH
    }
}