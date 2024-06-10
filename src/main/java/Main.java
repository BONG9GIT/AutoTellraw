import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (e.getPlayer().getScoreboardTags().contains("tellraw")){
            e.setCancelled(true);

            Bukkit.getScheduler().runTask(this, () -> {
                String str = e.getMessage();
                if (str.startsWith("http")){
                    net.md_5.bungee.api.chat.TextComponent textComponent = new TextComponent(ChatColor.GRAY + "[" + e.getPlayer().getName() + "의 링크]");
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, str));
                    Bukkit.spigot().broadcast(textComponent);
                }
                else{
                    str = str.replace("\\n", "\n");
                    str = ChatColor.translateAlternateColorCodes('&', str);
                    Bukkit.broadcastMessage(translateHexColorCodes("&#", "", str));
                }
            });
        }
    }



    public String translateHexColorCodes(String startTag, String endTag, String message) {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR  + group.charAt(0) + ChatColor.COLOR_CHAR  + group.charAt(1)
                    + ChatColor.COLOR_CHAR  + group.charAt(2) + ChatColor.COLOR_CHAR  + group.charAt(3)
                    + ChatColor.COLOR_CHAR  + group.charAt(4) + ChatColor.COLOR_CHAR  + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}
