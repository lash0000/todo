## Wanna learn to contribute? (Basic Mode)

`Translation: Taglish`

Here in this project we use JavaFX to initialize in Visual Studio Code as typically IDE
instead of Netbeans btw (who the hell use it still for now?)

So ngayon need natin maka-install muna ng two:

- OpenJDK (JavaFX library)
- Git (para maka-collab hehe)

### Eto yung mga direct links para ma-DL

```sh
https://gluonhq.com/products/javafx/
```
```sh
https://git-scm.com/download/win
```

### Tutorials (asa gDrive yung full)

<a href="https://drive.google.com/file/d/1R2DPM-IA6nz7gia9_Y3N7ndr5z5z25tO/view?usp=drive_link" target="_blank">
 Google Drive (watch mo)
</a>

### vmArgs (add this line)

Para ma-locate mismo ni VSCode kung saan nakalagay yung mga na-download mo na JAR files galing kay OpenJDK

```sh
"vmArgs": "--module-path \"Your Path To The lib Folder" --add-modules javafx.controls,javafx.fxml"
```

For example para sa vmArgs for the JVM gan2 (nasa .vscode setup q doon much better WAG BAGUHIN)

```sh
"vmArgs": "--module-path \"D:\\Users\\rogu\\Downloads\\openjfx-21.0.1_windows-x64_bin-sdk\\javafx-sdk-21.0.1\\lib\" --add-modules javafx.controls,javafx.fxml"
```