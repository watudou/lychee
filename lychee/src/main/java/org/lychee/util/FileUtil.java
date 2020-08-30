package org.lychee.util;

import lombok.extern.slf4j.Slf4j;
import org.lychee.bean.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 文件删除
 *
 * @author yons
 */
@Slf4j
public class FileUtil {

    private static int number = 0;
    private static int maxNumber = 999;

    /**
     * 保存文件
     */
    private static boolean saveFile(MultipartFile file, String filePath) {
        try {
            File f = new File(filePath);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            file.transferTo(new File(filePath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件相对路径"/yyyy/m/d/xx.x"
     *
     * @param suffix 后缀
     */
    private static String getFileRelativePath(String suffix) {
        Calendar ca = Calendar.getInstance();
        String fileRelativePath = new StringBuffer(File.separator).append(ca.get(Calendar.YEAR)).append(File.separator)
                .append(ca.get(Calendar.MONTH) + 1).append(File.separator).append(ca.get(Calendar.DAY_OF_MONTH))
                .append(File.separator).append(getFileName()).append(suffix).toString();
        return fileRelativePath;
    }

    private static String getFileName() {
        long fileName = System.currentTimeMillis();
        if (number > maxNumber) {
            number = 0;
        }
        number++;
        return String.valueOf(fileName) + number;
    }

    /**
     * 单文件上传
     *
     * @param file         文件
     * @param uploadPath   保存路径
     * @param uploadDomain 域名
     */
    public static FileInfo uploadSingle(MultipartFile file, String uploadPath, String uploadDomain) {
        FileInfo info = new FileInfo();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String relativePath = getFileRelativePath(suffix);
        String name = file.getOriginalFilename();
        if (name.length() > 25) {
            name = file.getOriginalFilename().substring(0, 20) + "...";
        }
        info.setName(name);
        info.setUrl(uploadDomain + relativePath);
        info.setSuffix(suffix);
        info.setSize(file.getSize());
        if (saveFile(file, uploadPath + relativePath)) {
            return info;
        }
        return null;
    }

    /**
     * 多文件上传
     *
     * @param files        文件
     * @param uploadPath   保存路径
     * @param uploadDomain 域名
     */
    public List<FileInfo> uploadBatch(MultipartFile[] files, String uploadPath, String uploadDomain) {
        List<FileInfo> list = new ArrayList<>();
        for (MultipartFile file : files) {
            FileInfo info = new FileInfo();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String relativePath = getFileRelativePath(suffix);
            if (saveFile(file, uploadPath + relativePath)) {
                info.setName(file.getOriginalFilename());
                info.setUrl(uploadDomain + relativePath);
                info.setSuffix(suffix);
                info.setSize(file.getSize());
                list.add(info);
            }
        }
        return list;
    }

    /**
     * 上传base64图片
     *
     * @param image
     * @param uploadPath
     * @return
     */
    public String uploadBase64(byte[] image, String uploadPath) {
        String relativePath = getFileRelativePath(".jpg");
        String filePath = uploadPath + relativePath;
        File f = new File(filePath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            for (int i = 0; i < image.length; ++i) {
                if (image[i] < 0) {
                    image[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(filePath);
            out.write(image);
            out.flush();
            out.close();
            return relativePath;
        } catch (Exception e) {
            return null;
        }
    }
}
