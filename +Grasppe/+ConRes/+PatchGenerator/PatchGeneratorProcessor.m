classdef PatchGeneratorProcessor < Grasppe.ConRes.PatchGenerator.Processors.Process
  %PATCHGENERATORPROCESSOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    PatchProcessor  = Grasppe.ConRes.PatchGenerator.Processors.Patch;
    ScreenProcessor = Grasppe.ConRes.PatchGenerator.Processors.Screen;
    PrintProcessor  = Grasppe.ConRes.PatchGenerator.Processors.Print;
    ScanProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Scan;
  end
  
  methods
    function Run(obj)
      obj.PatchProcessor.Parameters = obj.Parameters.Patch;
      obj.ScreenProcessor.Parameters = obj.Parameters.Screen;
      obj.PrintProcessor.Parameters = obj.Parameters.Print;
      obj.ScanProcessor.Parameters = obj.Parameters.Scan;
      
      % ffScreening( contone, resolution, title, spi, lpi, dpi, angles, noise)
      reference = obj.Parameters.Patch.MeanTone;
      contrast = obj.Parameters.Patch.Contrast;
      resolution = obj.Parameters.Patch.Resolution;
      addressability = obj.Parameters.Screen.Addressability;
      screenlpi   = obj.Parameters.Screen.Resolution;
      screenangle = obj.Parameters.Screen.Angle;
      scanpdi     = obj.Parameters.Scan.Resolution;
      width = 256;
      
      [img s] = Grasppe.Kit.ConRes.GeneratePatchImage(reference, contrast, resolution); %, width, addressability);
      
      patchImage    = im2double(img);
      screenedImage = grasppeScreen3(patchImage, scanpdi, addressability, screenlpi, screenangle );
      printedImage  = im2double(screenedImage);
      scannedImage  = im2double(printedImage);
      
      % imagePath, ppi, spi, lpi, angle
      
      subplot(1,1,1);
      
      s = warning('off', 'Images:initSize:adjustingMag');
      imshow(scannedImage,'InitialMagnification', 100, 'Border', 'loose');
      warning(s);
      
      set(gcf,'Name',toString(s)); 
      
      drawnow expose update;
      % imshow(imfilter(im2double(grasppeScreen3(img, addressability)), H),'InitialMagnification', 100); set(gcf,'Name',toString(s)); drawnow expose update;
    end
  end
  
end

