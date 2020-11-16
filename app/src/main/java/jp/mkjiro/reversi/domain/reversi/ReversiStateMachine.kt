package jp.mkjiro.reversi.domain.reversi

abstract class ReversiStateMachine {
    protected lateinit var state: State

    protected abstract fun runInitPhase()
    protected abstract fun runJudgeFirstPhase()
    protected abstract fun runJudgeSecondPhase()
    protected abstract fun runPlayerPhase()

    protected fun emitEvent(event: Event) {
        when (state) {
            State.INIT -> {
                when (event) {
                    Event.START -> {
                        changeState(State.JUDGE_FIRST)
                    }
                }
            }
            State.JUDGE_FIRST -> {
                when (event) {
                    Event.CONTINUE -> {
                        changeState(State.PLAYER_TURN)
                    }
                    Event.NOT_PUT -> {
                        changeState(State.JUDGE_SECOND)
                    }
                }
            }
            State.JUDGE_SECOND -> {
                when (event) {
                    Event.CONTINUE -> {
                        changeState(State.PLAYER_TURN)
                    }
                    Event.NOT_PUT -> {
                        changeState(State.FINISH)
                    }
                }
            }
            State.PLAYER_TURN -> {
                when (event) {
                    Event.HUMAN -> {
                        changeState(State.TURN_OF_HUMAN)
                    }
                    Event.CPU -> {
                        changeState(State.TURN_OF_CPU)
                    }
                }
            }
            State.TURN_OF_CPU -> {
                when (event) {
                    Event.FINISH -> {
                        changeState(State.JUDGE_FIRST)
                    }
                }
            }
            State.TURN_OF_HUMAN -> {
                when (event) {
                    Event.PUT -> {
                        changeState(State.PROCESSING)
                    }
                }
            }
            State.PROCESSING -> {
                when (event) {
                    Event.FINISH -> {
                        changeState(State.JUDGE_FIRST)
                    }
                }
            }
        }
    }

    protected fun setFirstState() {
        this.state = State.INIT
        onStateChanged()
    }

    private fun changeState(state: State) {
        this.state = state
        onStateChanged()
    }

    private fun onStateChanged() {
        when (state) {
            State.INIT -> {
                runInitPhase()
            }
            State.JUDGE_FIRST -> {
                runJudgeFirstPhase()
            }
            State.JUDGE_SECOND -> {
                runJudgeSecondPhase()
            }
            State.PLAYER_TURN -> {
                runPlayerPhase()
            }
            State.PROCESSING -> {
            }
            State.FINISH -> {
            }
            else -> {
            }
        }
    }

    enum class Event {
        START,
        FINISH,
        HUMAN,
        CPU,
        CONTINUE,
        PUT,
        NOT_PUT,
    }

    enum class State {
        INIT,
        JUDGE_FIRST,
        JUDGE_SECOND,
        PROCESSING,
        PLAYER_TURN,
        TURN_OF_HUMAN,
        TURN_OF_CPU,
        FINISH
    }
}