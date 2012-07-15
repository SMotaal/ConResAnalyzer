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
      try obj.PatchProcessor.Parameters = obj.Parameters.Patch; end
      try obj.ScreenProcessor.Parameters = obj.Parameters.Screen; end
      try obj.PrintProcessor.Parameters = obj.Parameters.Print; end
      try obj.ScanProcessor.Parameters = obj.Parameters.Scan; end
      
      % ffScreening( contone, resolution, title, spi, lpi, dpi, angles, noise)
      
      parameters = obj.Parameters;
      
      try
        % Patch
        reference       = findField(parameters.Patch, 'mean');
        contrast        = findField(parameters.Patch, 'contrast');
        resolution      = findField(parameters.Patch, 'resolution');
        size            = findField(parameters.Patch, 'size');
        
        % Screening
        addressability  = findField(parameters.Screen, 'addressability');        
        screenlpi       = findField(parameters.Screen, 'resolution');
        screenangle     = findField(parameters.Screen, 'angle');
        
        % Printing
        tvi             = findField(parameters.Print, 'gain');
        noise           = findField(parameters.Print, 'noise');
        blur            = findField(parameters.Print, 'blur');
        spread          = findField(parameters.Print, 'radius');
        
        % Scanning
        scanpdi         = findField(parameters.Scan, 'resolution');
        scanscale       = findField(parameters.Scan, 'scale');
        
    
        % Overloads
        width           = 256;
        
        [img s] = Grasppe.Kit.ConRes.GeneratePatchImage(reference, contrast, resolution); %, width, addressability);
        
        patchImage    = im2double(img);
        screenedImage = grasppeScreen3(patchImage, scanpdi, addressability, screenlpi, screenangle );
        printedImage  = im2double(screenedImage);
        scannedImage  = im2double(printedImage);
        
        % imagePath, ppi, spi, lpi, angle
        
        subplot(2,3,[1 2 4 5]);
        
        s = warning('off', 'Images:initSize:adjustingMag');
        imshow(scannedImage,'InitialMagnification', 100, 'Border', 'loose');
        warning(s);
        
        set(gcf,'Name',toString(s));
      end
      
      
      
      drawnow expose update;
      % imshow(imfilter(im2double(grasppeScreen3(img, addressability)), H),'InitialMagnification', 100); set(gcf,'Name',toString(s)); drawnow expose update;
    end
  end
  
end

