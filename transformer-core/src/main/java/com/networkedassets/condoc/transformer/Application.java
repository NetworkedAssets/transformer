package com.networkedassets.condoc.transformer;

import com.google.common.collect.ImmutableSet;
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.schedule.core.boundary.ScheduleInfoManager;
import com.networkedassets.condoc.transformer.schedule.util.ScheduleUtils;
import com.networkedassets.condoc.transformer.security.Role;
import io.swagger.jaxrs.config.BeanConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ApplicationPath;
import java.io.*;
import java.util.Optional;

@ApplicationPath("rest")
public class Application extends javax.ws.rs.core.Application {
    @Inject
    UserGroupManager userGroupManager;

    @Inject
    UserManager userManager;

    @Inject
    ScheduleInfoManager scheduleInfoManager;

    @Inject
    @Named("condocTransformerScheduler")
    Scheduler scheduler;

    private final static String AUTH_SECRET_PATH = System.getProperty("jboss.server.data.dir") + "/" + "auth_key.ser";

    public final static String AUTH_SECRET;

    static {
        String key = null;
        try (FileInputStream fis = new FileInputStream(AUTH_SECRET_PATH); ObjectInputStream ois = new ObjectInputStream(fis)) {
            key = (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            key = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
            serializeKeyToFile(key);
        } finally {
            AUTH_SECRET = key;
        }
    }

    private static void serializeKeyToFile(String key) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(AUTH_SECRET_PATH));
            oos.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void setupApplication() {
        prepareDatabase();
        prepareSwagger();
        rescheduleJobsFromDatabase(scheduler);
        printLogo();
        printTitle();

    }

    private void prepareDatabase() {
        createAllUsersGroupIfAbsent();
        createAdminsGroupIfAbsent();
        createAdminUserIfAbsent();
    }

    private void createAllUsersGroupIfAbsent() {
        UserGroup userGroup = userGroupManager
                .getByName("all")
                .orElse(new UserGroup("all").setAutomaticallyCreated(true)
                        .setManualUserAssignementAllowed((false)))
                .setRoles(ImmutableSet.of(Role.DOC_VIEWER));
        userGroupManager.merge(userGroup);
    }


    private void createAdminsGroupIfAbsent() {
        UserGroup adminGroup = userGroupManager
                .getByName("admins")
                .orElse(new UserGroup("admins")).setAutomaticallyCreated(true)
                .setRoles(ImmutableSet.of(Role.SYS_ADMIN, Role.DOC_EDITOR, Role.DOC_VIEWER));
        userGroupManager.merge(adminGroup);

    }

    private void createAdminUserIfAbsent() {
        Optional<User> admin = userManager
                .getByUsername("admin");
        if (!admin.isPresent()) {
            User user = new User("admin")
                    .setPassword("admin");
            userManager.persist(user);
            userGroupManager.addUserToGroup(user, userGroupManager.getByName("admins").get().getId());
        }

    }

    private void prepareSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Transformer API");
        beanConfig.setDescription("Transformer is nice");
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/transformer/rest");
        beanConfig.setResourcePackage("com.networkedassets.condoc.transformer");
        beanConfig.setScan(true);
    }

    private void rescheduleJobsFromDatabase(Scheduler scheduler) {
        scheduleInfoManager.getList().forEach(
                scheduleInfo -> {
                    try {
                        ScheduleUtils.schedule(scheduleInfo, scheduler);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        if (!e.getMessage().contains("will never fire")) throw new TransformerException(e);
                    }
                }
        );
    }

    private void printLogo() {
        System.out.println("" +
                "                                                                                             \n" +
                "    TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT           \n" +
                "  TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT         \n" +
                " TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT        \n" +
                " TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT        \n" +
                " TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT        \n" +
                "  TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT         \n" +
                "   TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT          \n" +
                "     TTTTTTTTTTTTTTTTTTT                                     TTTTTTTTTTTTTTTTTTTT            \n" +
                "      TTTTTTTTTTTTTTTTTTT                                    TTTTTTTTTTTTTTTTTTT             \n" +
                "        TTTTTTTTTTTTTTTTTTT                                TTTTTTTTTTTTTTTTTTT               \n" +
                "         TTTTTTTTTTTTTTTTTTT                              TTTTTTTTTTTTTTTTTT                 \n" +
                "           TTTTTTTTTTTTTTTTTTT                           TTTTTTTTTTTTTTTTT                   \n" +
                "            TTTTTTTTTTTTTTTTTTT                        TTTTTTTTTTTTTTTTTTT                   \n" +
                "              TTTTTTTTTTTTTTTTTTT                    TTTTTTTTTTTTTTTTTT                      \n" +
                "               TTTTTTTTTTTTTTTTTTT                  TTTTTTTTTTTTTTTTTT                       \n" +
                "                 TTTTTTTTTTTTTTTTTTT               TTTTTTTTTTTTTTTTT                         \n" +
                "                  TTTTTTTTTTTTTTTTTTT            TTTTTTTTTTTTTTTTTT                          \n" +
                "                    TTTTTTTTTTTTTTTTTTT        TTTTTTTTTTTTTTTTTT                            \n" +
                "                     TTTTTTTTTTTTTTTTTTT      TTTTTTTTTTTTTTTTTT                             \n" +
                "                       TTTTTTTTTTTTTTTTTTT   TTTTTTTTTTTTTTTTT                               \n" +
                "                        TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT                                \n" +
                "                          TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT                                 \n" +
                "                           TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT                                   \n" +
                "                             TTTTTTTTTTTTTTTTTTTTTTTTTTTT                                    \n" +
                "                              TTTTTTTTTTTTTTTTTTTTTTTTT                                      \n" +
                "                                TTTTTTTTTTTTTTTTTTTTT                                        \n" +
                "                                 TTTTTTTTTTTTTTTTTTTT                                        \n" +
                "                                TTTTTTTTTTTTTTTTTTTTTT                                       \n" +
                "                              TTTTTTTTTTTTTTTTTTTTTTTTT                                      \n" +
                "                             TTTTTTTTTTTTTTTTTTTTTTTTTTTT                                    \n" +
                "                           TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT                                   \n" +
                "                          TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT                                 \n" +
                "                        TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT                                \n" +
                "                       TTTTTTTTTTTTTTTTTTT   TTTTTTTTTTTTTTTTTT                              \n" +
                "                     TTTTTTTTTTTTTTTTTTT      TTTTTTTTTTTTTTTTTT                             \n" +
                "                    TTTTTTTTTTTTTTTTTTT        TTTTTTTTTTTTTTTTTTT                           \n" +
                "                  TTTTTTTTTTTTTTTTTTT            TTTTTTTTTTTTTTTTTT                          \n" +
                "                 TTTTTTTTTTTTTTTTTTT               TTTTTTTTTTTTTTTTTT                        \n" +
                "               TTTTTTTTTTTTTTTTTTT                  TTTTTTTTTTTTTTTTTT                       \n" +
                "              TTTTTTTTTTTTTTTTTTT                     TTTTTTTTTTTTTTTTTT                     \n" +
                "            TTTTTTTTTTTTTTTTTTT                        TTTTTTTTTTTTTTTTTT                    \n" +
                "           TTTTTTTTTTTTTTTTTTT                           TTTTTTTTTTTTTTTTTT                  \n" +
                "         TTTTTTTTTTTTTTTTTTT                              TTTTTTTTTTTTTTTTTT                 \n" +
                "        TTTTTTTTTTTTTTTTTTT                                TTTTTTTTTTTTTTTTTTT               \n" +
                "      TTTTTTTTTTTTTTTTTTT                                    TTTTTTTTTTTTTTTTTT              \n" +
                "     TTTTTTTTTTTTTTTTTTT                                      TTTTTTTTTTTTTTTTTT             \n" +
                "   TTTTTTTTTTTTTTTTTTT                                          TTTTTTTTTTTTTTTTTT           \n" +
                "  TTTTTTTTTTTTTTTTTTT                                            TTTTTTTTTTTTTTTTTT          \n" +
                " TTTTTTTTTTTTTTTTTT                                                TTTTTTTTTTTTTTTTTT        \n" +
                " TTTTTTTTTTTTTTTTT                                                  TTTTTTTTTTTTTTTT         \n" +
                " TTTTTTTTTTTTTTT                                                      TTTTTTTTTTTTTTT        \n" +
                "  TTTTTTTTTTTTT                                                        TTTTTTTTTTTTT         \n" +
                "    TTTTTTTTT                                                            TTTTTTTTT           \n" +
                "                                                                               "
        );
    }

    private void printTitle() {
        System.out.println("" +
                "________/\\\\\\\\\\\\\\\\\\______________________________/\\\\\\\\\\\\\\\\\\\\\\\\________________________________        \n" +
                " _____/\\\\\\////////______________________________\\/\\\\\\////////\\\\\\______________________________       \n" +
                "  ___/\\\\\\/_______________________________________\\/\\\\\\______\\//\\\\\\_____________________________      \n" +
                "   __/\\\\\\_________________/\\\\\\\\\\_____/\\\\/\\\\\\\\\\\\___\\/\\\\\\_______\\/\\\\\\_____/\\\\\\\\\\________/\\\\\\\\\\\\\\\\_     \n" +
                "    _\\/\\\\\\_______________/\\\\\\///\\\\\\__\\/\\\\\\////\\\\\\__\\/\\\\\\_______\\/\\\\\\___/\\\\\\///\\\\\\____/\\\\\\//////__    \n" +
                "     _\\//\\\\\\_____________/\\\\\\__\\//\\\\\\_\\/\\\\\\__\\//\\\\\\_\\/\\\\\\_______\\/\\\\\\__/\\\\\\__\\//\\\\\\__/\\\\\\_________   \n" +
                "      __\\///\\\\\\__________\\//\\\\\\__/\\\\\\__\\/\\\\\\___\\/\\\\\\_\\/\\\\\\_______/\\\\\\__\\//\\\\\\__/\\\\\\__\\//\\\\\\________  \n" +
                "       ____\\////\\\\\\\\\\\\\\\\\\__\\///\\\\\\\\\\/___\\/\\\\\\___\\/\\\\\\_\\/\\\\\\\\\\\\\\\\\\\\\\\\/____\\///\\\\\\\\\\/____\\///\\\\\\\\\\\\\\\\_ \n" +
                "        _______\\/////////_____\\/////_____\\///____\\///__\\////////////________\\/////________\\////////__");
    }
}
