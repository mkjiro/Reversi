package jp.mkjiro.reversi.ui.reversi

import androidx.lifecycle.viewModelScope
import jp.mkjiro.reversi.base.BaseViewModel
import jp.mkjiro.reversi.domain.reversi.board.Coordinate
import jp.mkjiro.reversi.usecase.reversi.ReversiRepository
import jp.mkjiro.reversi.ui.livedata.EventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReversiViewModel @Inject constructor(
    reversiRepository: ReversiRepository
) : BaseViewModel<ReversiEvents>() {
    override val liveEvent =
        EventLiveData<ReversiEvents>()

    val reversi = reversiRepository.createHumVSCPU(8, 8)

    val rows: Int
        get() = reversi.getBoard().cells.size
    val columns: Int
        get() = reversi.getBoard().cells[0].size

    private val _turnPlayerName = Channel<String>(capacity = UNLIMITED)
    val turnPlayerName = _turnPlayerName.consumeAsFlow()

    private val _winnerPlayerName = Channel<String>(capacity = UNLIMITED)
    val winnerPlayerName = _winnerPlayerName.consumeAsFlow()

    fun setup() {
        reversi.turnPlayerName.map {
            val shownName = "$it's Turn"
            viewModelScope.launch {
                _turnPlayerName.send(shownName)
            }
        }.launchIn(viewModelScope)

        reversi.winnerPlayerName.map {
            val showName = "$it is Winner !!!"
            viewModelScope.launch {
                _winnerPlayerName.send(showName)
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.Default) {
            reversi.start()
        }
    }

    fun finish() {
        reversi.finish()
    }

    fun putPiece(position: Int) {
        var coordinate = Coordinate(
            position / columns,
            position % columns
        )
        viewModelScope.launch(Dispatchers.Default) {
            reversi.putPiece(coordinate)
        }
    }
}
