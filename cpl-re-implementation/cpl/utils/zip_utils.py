import os
import zipfile


def do_zip_compress(source_path, target_path):
    """
    压缩指定目录或文件，将其存放到指定目录
    :param source_path: 待压缩的目录或文件
    :param target_path: 存放压缩包的目录
    :return:
    """
    # print("原始文件/文件夹路径：" + source_path)
    output_name = f"{target_path}.zip" if not target_path.endswith('zip') else target_path
    parent_name = os.path.dirname(source_path)
    # print("压缩文件/文件夹所在目录：", parent_name)
    zip_file = zipfile.ZipFile(output_name, "w", zipfile.ZIP_DEFLATED)

    # 单文件压缩
    if os.path.isfile(source_path):
        relpath = os.path.relpath(source_path, parent_name)
        zip_file.write(source_path, relpath)
    else:
        # 多层级压缩
        for root, dirs, files in os.walk(source_path):
            # 跳过缓存目录
            if "__pycache__" in dirs:
                dirs.remove("__pycache__")
            # 跳过checkpoints目录
            if "checkpoints" in dirs:
                dirs.remove("checkpoints")
            # 压缩
            for file in files:
                if str(file).startswith("~$"):
                    continue
                filepath = os.path.join(root, file)
                if os.path.islink(filepath):
                    # print('跳过软连接：{}'.format(filepath))
                    continue
                # print("压缩文件路径：" + filepath)
                write_path = os.path.relpath(filepath, parent_name)  # 获取相对路径，这样的好处就是可以保留一层目录在zip文件中
                zip_file.write(filepath, write_path)
    zip_file.close()
