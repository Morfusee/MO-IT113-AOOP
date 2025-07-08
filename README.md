# Motorph Application Setup Guide

## Prerequisites

Before accessing the application, ensure you have the following installed:

1. **[Node.js](https://nodejs.org/en)** (Includes npm)
2. **[pnpm](https://pnpm.io/installation)**
3. **[Java Coding Pack for VSCode](https://code.visualstudio.com/docs/languages/java)**
4. **Language Support for Java(TM) by Red Hat Extension (version 1.41.1)**
   - The current stable version has an ongoing [issue](https://github.com/redhat-developer/vscode-java/issues/4050) with Lombok
5. **Visual Studio Code (VSCode) (Recommended)**
   - If you can run Java in another IDE while using VSCode for the frontend, you may skip this, but VSCode is preferred for consistency.

---

## Cloning the Repository

Once you have installed the prerequisites, clone this repository to your machine:

```sh
git clone https://github.com/Morfusee/MO-IT110-OOP.git
```

For more details on cloning a repository, check [GitHub's documentation](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository).

---

## Running the Frontend

1. Open the project folder in **VSCode**.
2. Open the terminal:
   - Press `Ctrl + J` to open the terminal.
3. Navigate to the frontend directory:
   
   ```sh
   cd frontend
   ```
4. Install the packages for the frontend
   ```sh
   npm install  # If using npm
   ```
   
   ```sh
   pnpm install  # If using pnpm
   ```
6. After installing the packages, start the frontend server:
   
   ```sh
   npm run dev  # If using npm
   ```
   
   ```sh
   pnpm run dev  # If using pnpm
   ```
7. The frontend server will start and provide a URL where you can access the application.

📌 **Example Screenshot**

![image](https://github.com/user-attachments/assets/a0cfb782-dd31-41f4-bdf3-f64fba835d86)


## Running the Backend

There are two ways to start the backend server:

### **Option 1: Using Another IDE**

If you prefer another Java-supported IDE, you can run the backend from there and then access the frontend normally.

### **Option 2: Using VSCode (Recommended)**

> 💡 **Note: Extension Downgrade Recommendation**
>
> You will encounter an ongoing [issue](https://github.com/redhat-developer/vscode-java/issues/4050) using **Language Support for Java(TM) by Red Hat**. It's recommended to downgrade it to version `1.41.1`.
>
> **Steps to Downgrade:**
>
> 1. Open the Extensions view (`Ctrl + Shift + X`)
> 2. Search for **Language Support for Java(TM) by Red Hat**
> 3. Click the ⚙️ gear icon next to the extension name
> 4. Select **"Install Specific Version..."**
> 5. Choose **`1.41.1`** from the list
> 6. Reload VS Code when prompted

1. Open **VSCode**, which will initialize the Java SDK.

![image](https://github.com/user-attachments/assets/d716c52e-cd75-4573-b040-0a52d5697a1a)

2. Wait until the Java SDK is fully loaded (indicated at the bottom of the VSCode window).

![image](https://github.com/user-attachments/assets/81ab15a5-80bb-45c2-b3cd-f479743dbf2f)

3. Click on any Java file, and a **Run** button should appear beside your open files.

![image](https://github.com/user-attachments/assets/4622802e-99f7-4c9c-85bd-4320fbf27f56)

4. Click the **Run** button, and a dropdown will appear.

![image](https://github.com/user-attachments/assets/91b07753-5e48-42c5-a815-1002167909b0)

5. Select `MotorphApplication.java`.
6. Once started, the console will display logs indicating that `MotorphApplication` has started successfully.

![image](https://github.com/user-attachments/assets/5fb87827-8beb-43cc-9cd0-79b621e9b71c)

---

### If You Encounter Java Issues in VSCode

Sometimes VSCode may still fail to properly run the Java application due to workspace cache problems. If that happens:

1. Press `Ctrl + Shift + P` to open the Command Palette.
2. Type and select **Java: Clean Java Language Server Workspace**.

![image](https://github.com/user-attachments/assets/bd3a2248-342b-4487-abde-90c2097ba100)

3. When prompted, click **Reload and Delete**.

![image](https://github.com/user-attachments/assets/6ce50098-eb85-46dc-be34-6608e6842db2)

4. VSCode will restart and clean the Java workspace.
5. Try running `MotorphApplication.java` again afterward.

---

## Logging into the Application

### Once both servers are running:

- Open the provided frontend URL in your browser.
- The **HR Employee's credentials** will be provided automatically.
- Use those credentials to log in and access the system.

### Logging in as an Employee

- Navigate to the **Backend folder** of the repository.
- Open the **src/main/resources directory** and locate **LoginCredentials.json**.
- Select an employee from the list and use the provided credentials to log in.

---

## Notes

- If you encounter errors, ensure all dependencies are installed properly.
- If Java SDK is not detected in VSCode, try restarting the editor and checking the extensions.
- If you need additional support, refer to the official documentation links provided in this guide.
