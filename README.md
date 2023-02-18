#  YADA


**Explore** | **Travel** | **Write**

[![Join the chat at https://gitter.im/YADA_/community](https://badges.gitter.im/YADA_/community.svg)](https://gitter.im/YADA_/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## About

Yet Another Diary App created for travelers who want to write about their experiences. Customizable themes with the fusion of maps and path tracking make it all the more interesting! 

## Contributing

The app is open for contribution during [PWOC](https://pwoc.vercel.app/). Start discussion in the PWOC Discord Server. 

To be able to use firebase services, like authentication, you will need to register your SHA-1 keys.
For that, make sure your Java's bin directory is stored in environmental variables of your PC. 
After that, enter this in your terminal:
```
keytool -list -v -keystore "c:\users\your_user_name\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android 
```
 and replace  your_user_name accordingly.
You can share the SHA-1 Key with your mentor, and they will add it in Firebase console.
If you face any problem in this step, make sure to contact the mentor.





# 🖱️ How to Contribute 

If you think that you can add a new feature or want to fix a bug, we invite you to contribute to Calculator and make this project better. To start contributing, follow the below instructions:

1. Create a folder at your desire location (usually at your desktop).

2. Open Git Bash Here

3. Create a Git repository.

   Run command `git init`

4. [Fork](https://github.com/ken1000minus7/YADA) the project. Click on the <a href="https://github.com/ken1000minus7/YADA.git/fork"><img src="https://i.imgur.com/G4z1kEe.png" height="15" width="15"></a> icon in the top right to get started.

5. Clone your forked repository of project.

```bash
git clone https://github.com/<your_username>/YADA.git
```

6. Navigate to the project directory.

```bash
cd YADA
```

7. Add a reference(remote) to the original repository.

```bash
git remote add upstream https://github.com/ken1000minus7/YADA.git
```

8. Check the remotes for this repository.

```bash
git remote -v
```

9. Always take a pull from the upstream repository to your main branch to keep it updated as per the main project repository.

```bash
git pull upstream main
```

10. Create a new branch(prefer a branch name that relates to your assigned issue).

```bash
git checkout -b <YOUR_BRANCH_NAME>
```

11. Perform your desired changes to the code base.

12. Check your changes.

```bash
git status
```

```bash
git  diff
```

13. Stage your changes.

```bash
git add . <\files_that_you_made_changes>
```

14. Commit your changes.

```bash
git commit -m "Commit Message"
```

15. Push the committed changes in your feature branch to your remote repository.

```bash
git push -u origin <your_branch_name>
```

16. To create a pull request, click on `compare and pull requests`.

17. Add an appropriate title and description to your PR explaining your changes.

18. Click on `Create pull request`.

Congratulations🎉, you have made a PR to the YADA.
Wait for your submission to be accepted and your PR to be merged by a maintainer.

## 🫴 How to Do Your First Pull Request?  
   ***(We are providing some Resource from where you can Learn)***

1. [Learn from Video](https://www.youtube.com/watch?v=nkuYH40cjo4)
2. [Open Source Guide](https://opensource.guide/how-to-contribute/)

## Code of Conduct

- [Code of Conduct](CODE_OF_CONDUCT.md)

## 🙏🏽 Support

This project needs a star️ from you. Don't forget to leave a star✨
Follow my Github for content
<br>
<br>
<hr>
<h6 align="center">© ken1000minus7 20223
<br>
 
## Roadmap

- Complete Kotlin Supremacy
- Introduction of ViewModels and implementation of MVVM architecture
- UI Overhauls

## Issues

If you think you have found a bug, please report it on [Issues](https://github.com/ken1000minus7/YADA/issues). The app is under active development and some new features are planned. You can suggest and vote for new features in the same location.

## FIGMA File

https://www.figma.com/file/VVNKBMvON3t4gZXpNpDNeS/first-page?node-id=0%3A1&t=xcpMwzDkU5J449RP-1

<img src="https://user-images.githubusercontent.com/78747188/144723438-c91a196a-d486-4f79-adaa-57d91a172052.png" width=200>


