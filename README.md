java生成aar包
1. 在android studio中新建一个jar包工程，将相关增量更新的.so文件和jnilibs放入，然后编写java接口即可，其中我自己间的目录如下：
[图片]
2. 其中java里面的com.github.sisong这个package名得和jni层的c++代码对应才行，想改的话需要配合一起改。例如hpatch_jni.c里面的jni函数接口是：
// hpatch_jni.c
// Created by sisong on 2019-09-30.
#include <jni.h>
#include "hpatch.h"
#ifdef __cplusplus
extern "C" {
#endif
    #define HPATCH_OPTIONS_ERROR 1
    #define _check_rt(v)              do { if (!(v)) { result=HPATCH_OPTIONS_ERROR; goto _clear; }; } while(0)
    #define __j2cstr_(jstr,cstr)      do { (cstr)=(*jenv)->GetStringUTFChars(jenv,jstr,NULL); _check_rt(cstr); } while(0)
    #define _check_j2cstr(jstr,cstr)  do { _check_rt(jstr); __j2cstr_(jstr,cstr); } while(0)
    #define _check_jn2cstr(jstr,cstr) do { if (jstr) __j2cstr_(jstr,cstr); else (cstr)=0; } while(0)
    #define _jrelease_cstr(jstr,cstr) do { if (cstr) (*jenv)->ReleaseStringUTFChars(jenv,jstr,cstr); } while(0)

    JNIEXPORT jint
    Java_com_github_sisong_HPatch_patch(JNIEnv* jenv,jobject jobj,
                                        jstring oldFileName,jstring diffFileName,
                                        jstring outNewFileName,jlong cacheMemory){
        const char* cOldFileName   =0;
        const char* cDiffFileName  =0;
        const char* cOutNewFileName=0;
        int result=0;

        _check_jn2cstr(oldFileName,cOldFileName);
        _check_j2cstr(diffFileName,cDiffFileName);
        _check_j2cstr(outNewFileName,cOutNewFileName);
        result=hpatchz(cOldFileName,cDiffFileName,cOutNewFileName,(int64_t)cacheMemory);
    _clear:
        _jrelease_cstr(outNewFileName,cOutNewFileName);
        _jrelease_cstr(diffFileName,cDiffFileName);
        _jrelease_cstr(oldFileName,cOldFileName);
        return result;
    }
    
#ifdef __cplusplus
}
#endif
如果要改成a.b.c的package结构，就需要把对应的jni接口改成Java_a_b_c_HPatch_patch的格式。

3. gradle配置方面唯一要添加的dependencies就是
implementation 'androidx.core:core:1.6.0'
加在module层级的build.gradle里面
[图片]
4. java层目前的接口和使用可见增量更新java层api说明
5. 上述配置没问题之后就可以出aar包了，直接使用下面命令在terminal输入下就可以
 ./gradlew clean
 ./gradlew assembleRelease
[图片]
