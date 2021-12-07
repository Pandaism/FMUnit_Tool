package com.pandaism.util.file;


import com.barcodelib.barcode.Linear;
import com.pandaism.FMUnitTool;
import com.pandaism.util.typing.Devices;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelManager {
    private TableView tableView;
    private String salesOrder;
    private String savePath;
    private Devices devices;
    private Workbook workbook;

    public ExcelManager(TableView tableView, String salesOrder, String savePath, Devices devices) {
        this.tableView = tableView;
        this.salesOrder = salesOrder;
        this.savePath = savePath;
        this.devices = devices;

        new Thread(() -> {
            initializeWorkbook();
            writeData();
            write();
        }).start();
    }

    private void initializeWorkbook() {
        this.workbook = new XSSFWorkbook();

        Sheet barcodeSheet = workbook.createSheet("Barcodes");

        Row header = barcodeSheet.createRow(0);
        for(int i = 0; i < this.tableView.getColumns().size(); i++) {
            header.createCell(i).setCellValue(((TableColumn) this.tableView.getColumns().get(i)).getText());
        }
    }

    private void writeData() {
        Sheet barcodeSheet = this.workbook.getSheet("Barcodes");
        int initialRow = 1;

        List<ObservableListWrapper<String>> recordables = this.tableView.getItems();
        for(int i = 0; i < recordables.size(); i++) {
            Row row = barcodeSheet.createRow(initialRow); //Row 2

            ObservableListWrapper<String> records = recordables.get(i);
            for(int j = 0; j < records.size(); j++) {
                if(!records.get(j).isEmpty()) {
                    Cell cell = CellUtil.createCell(row, j, records.get(j));

                    TableColumn tableColumn = (TableColumn) this.tableView.getColumns().get(j);
                    boolean serialized = this.devices.match(tableColumn.getText()).serializedProperty().get();

                    if(!serialized) {
                        CellRangeAddress cellAddresses = new CellRangeAddress(row.getRowNum(), row.getRowNum()+ 1, j, j);
                        barcodeSheet.addMergedRegion(cellAddresses);
                        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                    } else {
                        try {
                            Linear linear = new Linear();
                            linear.setData(records.get(j));
                            linear.setType(Linear.CODE128);

                            byte[] barcodeBytes = linear.renderBarcodeToBytes();
                            int pictureIdx = this.workbook.addPicture(barcodeBytes, Workbook.PICTURE_TYPE_PNG);
                            CreationHelper creationHelper = this.workbook.getCreationHelper();
                            Drawing drawing = barcodeSheet.createDrawingPatriarch();

                            barcodeSheet.setColumnWidth(j, 27 * 256);
                            barcodeSheet.createRow(row.getRowNum() + 1).setHeight((short) 600);

                            ClientAnchor anchor = creationHelper.createClientAnchor();
                            anchor.setRow1(row.getRowNum() + 1);
                            anchor.setCol1(j);
                            anchor.setRow2(row.getRowNum() + 2);
                            anchor.setCol2(j + 1);

                            drawing.createPicture(anchor, pictureIdx);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            initialRow+=2;
        }
    }

    private void write() {
        if(this.savePath.substring(this.savePath.lastIndexOf('.')).equals(".xlsx")) {
            try {
                File xlsx = new File(this.savePath);
                if(!xlsx.exists()) {
                    xlsx.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(xlsx.getAbsolutePath());
                this.workbook.write(outputStream);
                FMUnitTool.log.log(this.salesOrder + " data written to Excel.");
                this.workbook.close();
                FMUnitTool.log.log(this.salesOrder + " workbook closed.");
                outputStream.close();
                FMUnitTool.log.log(this.salesOrder + " outstream closed.");

                Platform.runLater(() -> {
                    JOptionPane.showMessageDialog(null, "SO" + this.salesOrder + " was successfully recorded.", "Record Created", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (IOException e) {
                FMUnitTool.log.log("File not found for " + this.salesOrder);
                Platform.runLater(() -> {
                    JOptionPane.showMessageDialog(null, "SO" + this.salesOrder + " failed to be recorded.", "Record Failed", JOptionPane.ERROR_MESSAGE);
                });
            }
        } else {
            FMUnitTool.log.log("ERROR: Unable to save Excel file for " + this.salesOrder + ". Please check save file is .xlsx extension.");
        }
    }
}
