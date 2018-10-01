import com.github.thomasnield.rxkotlinfx.bind
import com.github.thomasnield.rxkotlinfx.toObservable
import io.reactivex.rxkotlin.Observables
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.control.TextArea
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import tornadofx.*

fun main(args: Array<String>) {
    Application.launch(LineSetOperationsApp::class.java, *args)
}

class LineSetOperationsApp : App(LineSetOperationsView::class) {

}

class LineSetOperationsView : View("Line Set Operations") {

    var area1: TextArea by singleAssign()
    var area2: TextArea by singleAssign()
    var area3: TextArea by singleAssign()

    val group = ToggleGroup()
    var differenceToggle: ToggleButton by singleAssign()
    var unionToggle: ToggleButton by singleAssign()


    override val root = gridpane {
        row {
            vbox {
                label("List 1")
                area1 = textarea { }
            }
            vbox {
                label("List 2")
                area2 = textarea { }
            }
        }
        row {
            hbox {
                gridpaneConstraints { columnSpan = GridPane.REMAINING; alignment = Pos.CENTER }
                label("Result") { }
            }
        }
        row {
            area3 = textarea {
                gridpaneConstraints { columnSpan = GridPane.REMAINING; alignment = Pos.CENTER }
            }
        }
        row {
            vbox {
                gridpaneConstraints {
                    alignment = Pos.CENTER
                    columnSpan = GridPane.REMAINING
                }

                label("Operation")
                hbox {
                    differenceToggle = togglebutton("Difference", group)
                    unionToggle = togglebutton("Union", group)
                }
            }
        }
    }

    init {
        val content1 = area1.textProperty().toObservable()
        val content2 = area2.textProperty().toObservable()

        Observables.combineLatest(content1, content2, group.selectedToggleProperty().toObservable()).forEach {
            val lines1 = it.first.lines().toSet()
            val lines2 = it.second.lines().toSet()
            when (it.third) {
                differenceToggle -> {
                    area3.text = lines1.minus(lines2).joinToString("\n")
                }
                unionToggle -> {
                    area3.text = lines1.union(lines2).joinToString("\n")
                }
            }
        }
    }
}
