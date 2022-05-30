
![Logo](https://raw.githubusercontent.com/venkypandi/ZohoUsers/master/app/src/main/res/drawable/logo.jpg)


# Zoho Users with Paging 3 & RoomDB
![version](https://img.shields.io/badge/version-1.0.0-green) 

![issues](https://img.shields.io/github/issues/venkypandi/ZohoUsers)

The main purpose of this project is the implementation of Paging 3 library 
along with the RoomDb. Normally, we would use Paging Source for
fetching data but in this project we will look into the implementation of 
RemoteMeditor which will load data from RoomDB and remote simultaneously.
## API Reference

#### Base Url

```http
randomuser.me
```

#### Get random users
```http
  GET /api/?page=1&results=25&seed=zohousers
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `page` | `int` | Page number for Pagination |
| `results` | `int` | Load data value |
| `seed` | `string` | Any string value but returns same set of data |




## Authors

- [@venkypandi](https://www.github.com/venkypandi)


## Demo

![Demo](https://im.ezgif.com/tmp/ezgif-1-697e3adb86.gif)


## Features

- Shared Element Transitions
- Remote Mediator
- Connectivity Check
- Weather Backgrounds


## 🔗 Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/venkatesh-pandian-8143b4a1/)
[![twitter](https://img.shields.io/badge/twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/cryptolabindia)


## Tech Stack

**IDE:** Android Studio

**Langauage:** Kotlin

**Architecture:** Model View ViewModel

**Libraries:** Hilt, Retrofit, Glide, Paging3, RoomDB, Navigation Component


## Feedback

If you have any feedback, please reach out to me at picksoftwares@gmail.com


## References

https://developer.android.com/codelabs/android-paging#0

