package com.willbe.pipeline.obj;//package com.willbe.giftapp.appPipe.obj;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Callable;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import org.gradle.tooling.BuildLauncher;
//import org.gradle.tooling.GradleConnectionException;
//import org.gradle.tooling.GradleConnector;
//import org.gradle.tooling.ModelBuilder;
//import org.gradle.tooling.ProgressEvent;
//import org.gradle.tooling.ProgressListener;
//import org.gradle.tooling.ProjectConnection;
//import org.gradle.tooling.ResultHandler;
//import org.gradle.tooling.model.DomainObjectSet;
//import org.gradle.tooling.model.GradleProject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.comtop.cap.bm.metadata.build.gradle.ExecuteResult.Result;
//import com.comtop.cap.common.util.StringUtil;
//
///**
// * Gradle工具
// *
// * @author lizhongwen
// * @since jdk1.6
// * @version 2017年3月28日 lizhongwen
// */
//public final class GradleTools {
//
//    /** 日志 */
//    private static final Logger LOGGER = LoggerFactory.getLogger(GradleTools.class);
//
//    /** Gradle工具实例 */
//    private static GradleTools instance;
//
//    /** Gradle连接器 */
//    private GradleConnector gradleConnector;
//
//    /**
//     * 构造函数
//     */
//    private GradleTools() {
//
//    }
//
//    /**
//     * 构造函数
//     *
//     * @param gradleHome Gradle安装目录
//     */
//    private GradleTools(String gradleHome) {
//        gradleConnector = GradleConnector.newConnector();
//        if (StringUtil.isNotBlank(gradleHome)) {
//            File home = new File(gradleHome);
//            if (!home.exists()) {
//                throw new IllegalArgumentException("Gradle安装目录不存在！");
//            }
//            gradleConnector.useInstallation(home);
//        }
//    }
//
//    /**
//     * 获取gradle工具
//     *
//     * @param gradleHome gradle安装目录
//     * @return Gradle工具实例
//     */
//    public static synchronized final GradleTools getInstance(String gradleHome) {
//        if (instance == null) {
//            instance = new GradleTools(gradleHome);
//        }
//        return instance;
//    }
//
//    /**
//     * 转换为Eclipse工程
//     *
//     * @param rootProjectDir 根项目所在目录。
//     * @param subProjectName 子工程名称，如果子项目名称为空，则表示所有工程都转换为Eclipse工程
//     * @return 执行结果
//     */
//    public ExecuteResult toEclipseProject(String rootProjectDir, String subProjectName) {
//        return execute(rootProjectDir, subProjectName, "cleanEclipse", "eclipse");
//    }
//
//    /**
//     * eclipse中webapp项目buildpath增加其他工程的java文件和resource路径
//     *
//     * @param rootProjectDir 根项目所在目录。
//     * @param subProjectName 子工程名称，如果子项目名称为空，则表示所有工程都转换为Eclipse工程
//     * @return 执行结果
//     */
//    public ExecuteResult addProjectEClassPath(String rootProjectDir, String subProjectName) {
//        return execute(rootProjectDir, subProjectName, "addProjectEClassPath");
//    }
//
//    /**
//     * 编译Java代码，拷贝资源文件
//     *
//     * @param rootProjectDir 根项目所在目录。
//     * @param subProjectName 子项目名称，如果子项目名称为空，则表示编译所有
//     * @return 执行结果
//     */
//    public ExecuteResult compileJava(String rootProjectDir, String subProjectName) {
//        return execute(rootProjectDir, subProjectName, "classes", "testClasses");
//    }
//
//    /**
//     * 打Jar包
//     *
//     * @param rootProjectDir 根项目所在目录。
//     * @param subProjectName 子项目名称，如果子项目名称为空，则表示所有工程均打Jar包
//     * @return 执行结果
//     */
//    public ExecuteResult jar(String rootProjectDir, String subProjectName) {
//        return execute(rootProjectDir, subProjectName, "jar");
//    }
//
//    /**
//     * 执行任务
//     *
//     * @param rootProjectDir 根项目所在目录。
//     * @param subProjectName 子工程名称，如果子项目名称为空,则表示在整个工程中执行任务
//     * @param tasks 任务
//     * @return 执行结果
//     */
//    public ExecuteResult execute(String rootProjectDir, String subProjectName, String... tasks) {
//        if (StringUtil.isBlank(rootProjectDir)) {
//            throw new IllegalArgumentException("根项目所在的目录不能为空!");
//        }
//        final File root = new File(rootProjectDir);
//        if (!root.exists()) {
//            throw new IllegalArgumentException("根项目所在的目录不存在!");
//        }
//        if (tasks == null) {
//            throw new IllegalArgumentException("没有可执行的任务!");
//        }
//        final List<String> arguments = new ArrayList<String>(1);
//        arguments.add("-xTest");
//        final Map<String, String> jvmDefines = new HashMap<String, String>();
//        final String[] taskes = tasks;
//        if (StringUtil.isNotBlank(subProjectName)) {
//            String sub = this.findSubProject(root, subProjectName);
//            if (StringUtil.isNotBlank(sub)) {
//                for (int i = 0; i < taskes.length; i++) {
//                    taskes[i] = sub + ":" + tasks[i];
//                }
//            }
//        }
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        Future<ExecuteResult> future = service.submit(new Callable<ExecuteResult>() {
//
//            @Override
//            public ExecuteResult call() throws Exception {
//                return execute(root, arguments, jvmDefines, taskes);
//            }
//
//        });
//        try {
//            while (!future.isDone()) {
//                // 等待结果
//            }
//            LOGGER.info("{}::执行【{}】完成。", rootProjectDir, Arrays.toString(tasks));
//            return future.get();
//        } catch (Exception e) {
//            LOGGER.error("任务执行失败。", e);
//            return new ExecuteResult(Result.FAIL, e);
//        } finally {
//            service.shutdown();
//        }
//    }
//
//    /**
//     * 执行Gradle命令
//     *
//     * @param projectDir 项目目录
//     * @param arguments 执行参数
//     * @param jvmDefines jvm参数
//     * @param tasks 任务
//     * @return 执行结果
//     */
//    public ExecuteResult execute(File projectDir, List<String> arguments, Map<String, String> jvmDefines, String... tasks) {
//        ProjectConnection connection = gradleConnector.forProjectDirectory(projectDir).connect();
//        final CountDownLatch latch = new CountDownLatch(1);
//        final ExecuteResult ret = new ExecuteResult();
//        try {
//            BuildLauncher build = connection.newBuild().forTasks(tasks).withArguments(arguments.toArray(new String[arguments.size()]));
//            String[] jvmArgs = new String[jvmDefines.size()];
//            if (!jvmDefines.isEmpty()) {
//                int index = 0;
//                for (Map.Entry<String, String> entry : jvmDefines.entrySet()) {
//                    jvmArgs[index++] = "-D" + entry.getKey() + "=" + entry.getValue();
//                }
//            }
//            build.setJvmArguments(jvmArgs);
//            build.addProgressListener(new ProgressListener() {
//
//                @Override
//                public void statusChanged(ProgressEvent event) {
//                    LOGGER.info("execute gradle task status changed:" + event.getDescription());
//                }
//            });
//            build.run(new ResultHandler<Void>() {
//
//                @Override
//                public void onComplete(Void result) {
//                    ret.setResult(Result.SUCCESS);
//                    latch.countDown();
//                }
//
//                @Override
//                public void onFailure(GradleConnectionException exception) {
//                    ret.setResult(Result.FAIL);
//                    ret.setException(exception);
//                    latch.countDown();
//                }
//            });
//            latch.await();
//        } catch (InterruptedException e) {
//            LOGGER.error("执行Gradle命令出错:" + e.getMessage(), e);
//        } finally {
//            connection.close();
//        }
//        return ret;
//    }
//
//    /**
//     * 查找子项目
//     *
//     * @param root 根项目名称
//     * @param subName 子项目名称
//     * @return 子项目字符串
//     */
//    public String findSubProject(File root, String subName) {
//        ProjectConnection connection = gradleConnector.forProjectDirectory(root).connect();
//        try {
//            ModelBuilder<GradleProject> modelBuilder = connection.model(GradleProject.class);
//            GradleProject rootProject = modelBuilder.get();
//            return findSubProject(rootProject, null, subName);
//        } finally {
//            connection.close();
//        }
//    }
//
//    /**
//     * 子项目字符串
//     *
//     * @param parentProject 父项目
//     * @param parentName 父项目名称
//     * @param subName 子项目
//     * @return 子项目字符串
//     */
//    public String findSubProject(GradleProject parentProject, String parentName, String subName) {
//        DomainObjectSet<? extends GradleProject> children = parentProject.getChildren();
//        if (children == null || children.isEmpty()) {
//            return null;
//        }
//        Iterator<? extends GradleProject> iterator = children.iterator();
//        while (iterator.hasNext()) {
//            GradleProject project = iterator.next();
//            String name = parentName == null ? project.getName() : parentName + ":" + project.getName();
//            if (subName.equals(project.getName())) {
//                return name;
//            }
//            String result = findSubProject(project, name, subName);
//            if (StringUtil.isNotBlank(result)) {
//                return result;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * @param args xx
//     */
//    public static void main(String[] args) {
//        getInstance("D:/gradle-2.3").toEclipseProject("E:\book", null);
//    }
//
//}