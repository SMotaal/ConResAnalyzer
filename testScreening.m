SPI = [1200 1225 1270 1320 1440 1600 1760 1800 2250 2400 2450 2540 2800];
LPI = [75 100 115 125 133 137.5 150 162.5 166 175 200 208];


outputFolder      = fullfile('.', 'Output');

outputSubfolder   = fullfile(outputFolder, 'Screening Tests', 'BSW-SPILPI-N0');

mkdir(outputSubfolder);

imagePath             = [];
ppi                   = [];
angle                 = [];
printing.Noise        = 0;

for s = 1:numel(SPI)
  spi                 = SPI(s);
  
  for l = 1:numel(LPI)
    
    lpi               = LPI(l);
    
    dispf('Rendering Image:\t%1.1f spi\t%1.1f lpi...', spi,lpi);
    
    grasppeScreen3(imagePath, ppi, spi, lpi, angle);
    
    movefile(fullfile(outputFolder, 'BSW.tif'),  ...
      fullfile(outputSubfolder, ['BSW-' int2str(spi) 'SPI-' int2str(round(lpi*10)) 'LPI.tif']));
    
  end
end
