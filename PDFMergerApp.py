import sys
from PyPDF2 import PdfMerger
from PySide6.QtWidgets import (
    QApplication, QMainWindow, QVBoxLayout, QHBoxLayout,
    QWidget, QPushButton, QListWidget, QFileDialog, QMessageBox
)

class PDFMergerApp(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("PDF Merger")
        self.setGeometry(100, 100, 700, 500)

        #main vertical layout
        main_layout = QVBoxLayout()

        #step 1 add Files Button
        self.add_button = QPushButton("➕ Add PDF Files")
        self.add_button.clicked.connect(self.add_files)
        main_layout.addWidget(self.add_button)

        #step 2 list Widget to display file names
        self.file_list = QListWidget()
        main_layout.addWidget(self.file_list)

        #step 3 action buttons
        button_layout = QHBoxLayout()
        self.remove_button = QPushButton("Remove Selected")
        self.remove_button.clicked.connect(self.remove_file)
        button_layout.addWidget(self.remove_button)
        button_layout.addStretch()
        self.merge_button = QPushButton("Merge & Save")
        self.merge_button.clicked.connect(self.merge_files)
        button_layout.addWidget(self.merge_button)
        main_layout.addLayout(button_layout)

        central_widget = QWidget()
        central_widget.setLayout(main_layout)
        self.setCentralWidget(central_widget)

    def add_files(self):
        file_paths, _ = QFileDialog.getOpenFileNames(
            self,
            "Select PDF Files",
            "",
            "PDF Files (*.pdf)" # only look for pdf
        )
        if file_paths:
            self.file_list.addItems(file_paths)

    def remove_file(self):
        selected_item = self.file_list.currentItem()
        if selected_item:
            self.file_list.takeItem(self.file_list.row(selected_item))

    def merge_files(self):
        pdf_files = [self.file_list.item(i).text() for i in range(self.file_list.count())]

        if not pdf_files:
            QMessageBox.warning(self, "No PDF Files", "Please add PDF files to the list to merge.")
            return

        output_path, _ = QFileDialog.getSaveFileName(
            self,
            "Save Merged File As",
            "",
            "PDF Files (*.pdf)"
        )

        if output_path:
            if not output_path.lower().endswith('.pdf'):
                output_path += '.pdf'

            merger = PdfMerger()
            try:
                for pdf_path in pdf_files:
                    merger.append(pdf_path)
                
                merger.write(output_path)
                merger.close()
                
                QMessageBox.information(
                    self,
                    "Merge Successful",
                    f"Successfully merged {len(pdf_files)} PDF files to:\n{output_path}"
                )
            except Exception as e:
                QMessageBox.critical(
                    self,
                    "Merge Error",
                    f"An error occurred while merging:\n{e}"
                )

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = PDFMergerApp()
    window.show()
    sys.exit(app.exec())