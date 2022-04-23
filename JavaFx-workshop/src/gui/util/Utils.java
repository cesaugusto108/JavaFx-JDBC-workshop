package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    public static Stage currentStage(ActionEvent actionEvent) {

        return (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    }

    public static Integer stringParseInt(String string) {

        try {

            return Integer.parseInt(string);
        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static Double stringParseDouble(String string) {

        try {

            return Double.parseDouble(string);
        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {

        tableColumn.setCellFactory(x -> new TableCell<>() {

            @Override
            protected void updateItem(Double item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {

                    setText(null);
                } else {

                    Locale.setDefault(Locale.US);
                    setText(String.format("%." + decimalPlaces + "f", item));
                }
            }
        });
    }

    public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {

        tableColumn.setCellFactory(x -> new TableCell<>() {

            private final SimpleDateFormat SDF = new SimpleDateFormat(format);

            @Override
            protected void updateItem(Date item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {

                    setText(null);
                } else {

                    setText(SDF.format(item));
                }
            }
        });
    }

    public static void formatDatePicker(DatePicker datePicker, String format) {

        datePicker.setConverter(new StringConverter<>() {

            final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(format);

            {
                datePicker.setPromptText(format.toLowerCase());
            }

            @Override
            public String toString(LocalDate localDate) {

                if (localDate != null) {

                    return DATE_TIME_FORMATTER.format(localDate);
                } else {

                    return null;
                }
            }

            @Override
            public LocalDate fromString(String s) {

                if (s != null && !s.isEmpty()) {

                    return LocalDate.parse(s, DATE_TIME_FORMATTER);
                } else {

                    return null;
                }
            }
        });
    }
}
