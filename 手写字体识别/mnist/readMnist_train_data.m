clear all;
clc;
%��ȡѵ��ͼƬ�����ļ�
[FileName,PathName] = uigetfile('*.*','ѡ��ѵ��ͼƬ�����ļ�train-images.idx3-ubyte');
TrainFile = fullfile(PathName,FileName);
fid = fopen(TrainFile,'r');
a = fread(fid,16,'uint8');
MagicNum = ((a(1)*256+a(2))*256+a(3))*256+a(4);
ImageNum = ((a(5)*256+a(6))*256+a(7))*256+a(8);
ImageRow = ((a(9)*256+a(10))*256+a(11))*256+a(12);
ImageCol = ((a(13)*256+a(14))*256+a(15))*256+a(16);
if ((MagicNum~=2051)||(ImageNum~=60000))
    error('���� MNIST train-images.idx3-ubyte �ļ���');
    fclose(fid);    
    return;    
end
h_w = waitbar(0,'���Ժ򣬴�����>>');
for i=1:ImageNum
    mnist_train_images(i,:)= fread(fid,ImageRow*ImageCol,'uint8'); 
    waitbar(i/ImageNum);
end
save mnist_train_images.mat mnist_train_images
fclose(fid);
close(h_w);