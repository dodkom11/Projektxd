package wypozyczalnia.config;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SelectionModel;

public abstract class VetoListener<T> implements ChangeListener<T> {

    private final SelectionModel<T> selectionModel;
    private boolean changing = false;

    public VetoListener(SelectionModel<T> selectionModel) {
        if (selectionModel == null) {
            throw new IllegalArgumentException();
        }
        this.selectionModel = selectionModel;
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        if (!changing && isInvalidChange(oldValue, newValue)) {
            changing = true;
            Platform.runLater(() -> {
                selectionModel.select(oldValue);
                changing = false;
            });
        }
    }

    protected abstract boolean isInvalidChange(T oldValue, T newValue);

}