% function [ output_args ] = blockRetina( input_args )
%BLOCKRETINA Summary of this function goes here
%   Detailed explanation goes here

infolder      = '/Volumes/daflairsStoreQuattro/Output/Series104g/Resources/Contone/BlockImage/';
outfolder     = '~/Desktop/ConResLab/Inbox';

try FS.mkDir(outfolder); end

files         = { ...
  'RTV25-MM530-SPI2540-LPI175-DEG375-DPI2400', ...
  'RTV50-MM530-SPI2540-LPI175-DEG375-DPI2400',  ...
  'RTV75-MM530-SPI2540-LPI175-DEG375-DPI2400'};

SPI           = 2540;
LPI           = 100; %175;
DEG           = 37.5;
DPI           = 2400;
GF            = 3; % pi();
HVF           = Grasppe.ConRes.Math.VisualResolution(DPI); %*GF;

SCL           = 0.5;
PPI           = DPI*SCL;
PPM           = PPI/0.0254;

gaussian      = @(x, y)     imfilter(x,fspecial('gaussian',round(y*20), y),'replicate');

for m = 1:numel(files)
  filename    = files{m};
  fileext     = '.png';
  
  ctinpath    = fullfile(infolder, [filename fileext]);
  
  outname     = filename;
  outname     = regexprep(outname,'LPI\d+', ['LPI' num2str(LPI,'%3d')]);
  outname     = regexprep(outname,'SPI\d+', ['SPI' num2str(SPI,'%3d')]);
  outname     = regexprep(outname,'DEG\d+', ['DEG' num2str(round(DEG*10),'%3d')]);
  outname     = regexprep(outname,'DPI\d+', ['DPI' num2str(DPI,'%3d')]);
  
  htinpath    = fullfile(infolder,  [outname '-HT' fileext]);  
  
  ctimgpath   = fullfile(outfolder, [outname '-CT' fileext]);
  htimgpath   = fullfile(outfolder, [outname '-HT' fileext]);
  
  ctfltpath   = fullfile(outfolder, [outname '-CT-GF' int2str(GF) fileext]);
  htfltpath   = fullfile(outfolder, [outname '-HT-GF' int2str(GF) fileext]);
  

  %if exist(ctimgpath, 'file')>0
  %  ctimg     = imread(ctimgpath);
  %else
  ctimg       = imread(ctinpath);
  %end
  
  if exist(htinpath, 'file')>0
    htimg     = imread(htinpath);
  else
    htimg     = imresize(grasppeScreen3(ctimg, DPI, SPI, LPI, DEG), DPI/SPI);
    imwrite(htimg, htinpath, 'XResolution', DPI, 'YResolution', DPI);
  end
  
  ctflt       = ctimg;
  htflt       = htimg;
  
  for n = 1:GF
    ctflt       = gaussian(ctflt, HVF);
    htflt       = gaussian(htflt, HVF);
  end
  
  
  imwrite(imresize(htimg, SCL), htimgpath, 'XResolution', PPI, 'YResolution', PPI);
  imwrite(imresize(ctimg, SCL), ctimgpath, 'XResolution', PPI, 'YResolution', PPI);
  
  imwrite(imresize(ctflt, SCL), ctfltpath, 'XResolution', PPI, 'YResolution', PPI);
  imwrite(imresize(htflt, SCL), htfltpath, 'XResolution', PPI, 'YResolution', PPI);
  
end

%end
