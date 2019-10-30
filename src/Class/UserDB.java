package Class;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.io.FileOutputStream;
import java.util.List;


public class UserDB {

    static String path = "/home/fouriv/Documents/2_Year3/Computer_Network/Lab/BTL_1_v1/src/Class/User_Database.xlsx";

    public static int addRecord(User user) throws Exception {
        String DBpath = path;

        FileInputStream excelFile = null;
        XSSFWorkbook workbook = null;
        // excel files

        boolean exist = new File(DBpath).exists();
        if (exist) {
            File file = new File(DBpath);

            excelFile = new FileInputStream(file);
            workbook = new XSSFWorkbook(excelFile);

        } else {
            workbook = new XSSFWorkbook();
        }

        // Create Workbook instance holding reference to .xlsx file


        // Get desired sheet from the workbook
        XSSFSheet sheet = null;

        //Check if the sheet exist
        boolean sheet_exist = false;
        if (workbook.getNumberOfSheets() != 0) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                if (workbook.getSheetAt(i).getSheetName().matches("Users")) {
                    sheet = workbook.getSheet("Users");
                    sheet_exist = true;
                }
            }

            if (!sheet_exist) {
                sheet = workbook.createSheet("Users");
                Row row = sheet.createRow(0);
                Cell cell = row.createCell(0);
                cell.setCellValue("UserID");

                cell = row.createCell(1);
                cell.setCellValue("UserIP");

                cell = row.createCell(2);
                cell.setCellValue("UserPwd");

                cell = row.createCell(3);
                cell.setCellValue("UserPort");

                cell = row.createCell(4);
                cell.setCellValue("UserStatus");
            }
        } else {
            // Create new sheet to the workbook if empty
            sheet = workbook.createSheet("Users");
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("UserID");

            cell = row.createCell(1);
            cell.setCellValue("UserIP");

            cell = row.createCell(2);
            cell.setCellValue("UserPwd");

            cell = row.createCell(3);
            cell.setCellValue("UserPort");

            cell = row.createCell(4);
            cell.setCellValue("UserStatus");
        }


        int dest_row = sheet.getLastRowNum() + 1;
        sheet.createRow(dest_row);

        System.out.println("Add new record to Users Database.");

        Row row = sheet.getRow(dest_row);
        Cell cell = row.createCell(0);
        cell.setCellValue(user.getUsrID());

        cell = row.createCell(1);
        cell.setCellValue(user.getUsrIP());

        cell = row.createCell(2);
        cell.setCellValue(user.getUsrPasswd());

        cell = row.createCell(3);
        cell.setCellValue(user.getUsrPort());

        cell = row.createCell(4);
        cell.setCellValue(user.getUsrStatus());

        if (exist) {
            excelFile.close(); //Close the InputStream
        }

        FileOutputStream output_file = new FileOutputStream(new File(DBpath));  //Open FileOutputStream to write updates

        workbook.write(output_file); //write changes
        output_file.close();
        return dest_row;
    }

    public static ArrayList<User> retrieveUsrLst() throws Exception
    {
        FileInputStream excelFile = null;
        XSSFWorkbook workbook = null;
        // excel files

        File file = new File(path);

        excelFile = new FileInputStream(file);
        workbook = new XSSFWorkbook(excelFile);

        // Get desired sheet from the workbook
        XSSFSheet sheet = null;
        sheet = workbook.getSheetAt(0);

        ArrayList<User> list = new ArrayList<User>();

        if(sheet != null) {
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                User temp = new User();
                Row row = sheet.getRow(i);
                temp.setUsrID(row.getCell(0).getStringCellValue());
                temp.setUsrIP(row.getCell(1).getStringCellValue());
                temp.setUsrIP(row.getCell(2).getStringCellValue());
                temp.setUsrPort((int) row.getCell(3).getNumericCellValue());
                temp.setUsrStatus((int) row.getCell(4).getNumericCellValue());
                System.out.println(temp.getUsrID());
                list.add(0, temp);
            }
        }

        excelFile.close();
        return list;
    }


    public static void status_update(String _ID, int _Status) throws Exception
    {
        String DBpath = path;
        User target = new User();

        // excel files
        FileInputStream excelFile = new FileInputStream(new File(DBpath));

        // Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

        // Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        ArrayList<Row> filteredRows = new ArrayList<Row>();
        int row_num = 0;

        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell.getCellType() == CellType.STRING) {
                if (cell.getStringCellValue().equals(_ID)) {
                    row_num = row.getRowNum();
                }
            }
        }

        Row row = sheet.getRow(row_num);
        if(row_num != 0) {

            Cell cell = row.getCell(3);
            cell.setCellValue(_Status);

        }

        excelFile.close(); //Close the InputStream
        FileOutputStream output_file =new FileOutputStream(new File(DBpath));  //Open FileOutputStream to write updates
        workbook.write(output_file); //write changes
        output_file.close();
    }



    public static User searchID(String _ID) throws Exception
    {
        String DBpath = path;
        User target = new User();

        // excel files
        FileInputStream excelFile = new FileInputStream(new File(DBpath));

        // Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

        // Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        int row_num = 0;

        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell.getCellType() == CellType.STRING) {
                if (cell.getStringCellValue().equals(_ID)) {
                    row_num = row.getRowNum();
                }
            }
        }

        Row row = sheet.getRow(row_num);

        if(row_num != 0) {
            target.setUsrID(row.getCell(0).getStringCellValue());
            target.setUsrIP(row.getCell(1).getStringCellValue());
            target.setUsrPasswd(row.getCell(2).getStringCellValue());
            target.setUsrPort((int)row.getCell(3).getNumericCellValue());
            target.setUsrStatus((int)row.getCell(4).getNumericCellValue());
        }
        else {
            target.setUsrID("");
        }


        excelFile.close(); //Close the InputStream

        return target;

    }

    private int usrNumber;
    public User[] usrlist;

    public UserDB(int num ){
        usrlist = new User[num];
        usrNumber = num;
    }

    public int getBookNumber() {
        return usrNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.usrNumber = bookNumber;
    }
}