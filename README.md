# PlayerAccessList
### Плагин на белый/черный список


## 🔌 Как подключить API

Чтобы подключить плагин и его API к вашему проекту, скачайте плагин.  
В своём проекте создайте папку **libs**, поместите туда скачанный плагин и переименуйте его в **PlayerAccessList.jar**.

---

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>me.superchirok1</groupId>
        <artifactId>PlayerAccessList</artifactId>
        <version>ВАША ВЕРСИЯ</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/libs/PlayerAccessList.jar</systemPath>
    </dependency>
</dependencies>
```
### Gradle

```gradle
dependencies {
    implementation files('libs/PlayerAccessList.jar')
}
```

### PALAPI (PlayerAccessListAPI)

Класс позволяет подключаться к спискам.
```java
// pal — это экземпляр класса PlayerAccessList

new PALAPI(pal).getWhitelist(); // Белый список
new PALAPI(pal).getBlacklist(); // Чёрный список
```

➕ Пример добавления игрока в чёрный список
```java
new PALAPI(pal).getBlacklist().add("супер ник"); // UUID или ник игрока
```
### События: WhitelistCancelJoinEvent, BlacklistCancelJoinEvent

Эти классы — ивенты.

Пример: отправка всем игрокам сообщения о том, что кто-то пытался войти:
```java
public class JoinListener implements Listener {

    @EventHandler
    public void onWhitelistCancel(WhitelistCancelJoinEvent event) {

        Player player = event.getPlayer();

        String message = ChatColor.translateAlternateColorCodes('&',
                "&cИгрок &7" + player.getName() + "&c пытался войти, но не был в белом списке");

        Bukkit.broadcastMessage(message);
    }

}
```
