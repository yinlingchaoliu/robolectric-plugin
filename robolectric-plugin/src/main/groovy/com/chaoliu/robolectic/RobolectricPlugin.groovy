package com.chaoliu.robolectic

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * 编写groovy与gradle语法一样
 * gradle 是引入文件
 * groovy 是引入插件
 * robolecticPlugin
 *
 * @date 2019-3-28
 * @author chentong
 * @see 'https://docs.gradle.org'
 */
class RobolectricPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' chaoliu required.")
        }

        //chaoliu 配置
        project.android {
            defaultConfig {
                testInstrumentationRunner "android.test.runner.AndroidJUnitRunner"
            }

            testOptions {
                unitTests {
                    includeAndroidResources = true
                }
            }
        }

//      project.extensions.create('chaoliu', RobolectricExtension)

        project.dependencies {

            //junit
            testImplementation 'junit:junit:4.12'

            //mockito
            testImplementation "org.mockito:mockito-core:2.8.9"
            //powermock
            testImplementation "org.powermock:powermock-module-junit4:1.7.3"
            testImplementation "org.powermock:powermock-module-junit4-rule:1.7.3"
            testImplementation "org.powermock:powermock-api-mockito2:1.7.3"
            testImplementation "org.powermock:powermock-classloading-xstream:1.7.3"

            //robolectric
            testImplementation "org.robolectric:robolectric:3.8"
            testImplementation 'org.robolectric:shadows-support-v4:3.4-rc2'
            testImplementation "com.android.support.test:monitor:1.0.2"

            //mockwebserver
            testImplementation 'com.squareup.okhttp3:mockwebserver:3.9.1'
            //mock-restful
            testImplementation 'com.github.andrzejchm.RESTMock:android:0.3.1'

            //AssertJ
            testImplementation 'org.assertj:assertj-core:2.9.1'
            //Note that AssertJ 3.x requires at least Java 8 and AssertJ 2.x requires at least Java 7.
            //AssertJ-Android module:
            testImplementation 'com.squareup.assertj:assertj-android:1.2.0'
            testImplementation 'com.squareup.assertj:assertj-android-support-v4:1.2.0'
            testImplementation 'com.squareup.assertj:assertj-android-appcompat-v7:1.2.0'
            testImplementation 'com.squareup.assertj:assertj-android-design:1.2.0'
            testImplementation 'com.squareup.assertj:assertj-android-recyclerview-v7:1.2.0'

            //java.lang.VerifyError: class org.robolectric.android.fakes.RoboMonitoringInstrumentation overrides final method specifyDexMakerCacheProperty.()V
            testImplementation "com.android.support.test:runner:1.0.2"
            testImplementation "com.android.support.test:rules:1.0.2"

        }

        //buildscript 插件classpath 不能自动添加
        project.buildscript {
            repositories {
                jcenter()
                google()
                maven { url 'http://maven.aliyun.com/nexus/content/groups/public' }
                maven { url 'https://oss.sonatype.org/content/groups/public/' }
            }
        }

        project.allprojects {
            repositories {
                jcenter()
                google()
                maven { url "https://jitpack.io" }
            }
        }

        //https://docs.gradle.org/current/userguide/tutorial_using_tasks.html#configure-by-dag
        project.gradle.taskGraph.whenReady { taskGraph ->
            taskGraph.allTasks.each { task ->
                if (task.name.contains("test")) {
                    task.enabled = false
                }
            }
        }

    }

}