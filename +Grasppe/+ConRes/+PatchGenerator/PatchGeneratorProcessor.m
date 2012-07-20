classdef PatchGeneratorProcessor < Grasppe.Occam.Process
  %PATCHGENERATORPROCESSOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    PatchProcessor  = Grasppe.ConRes.PatchGenerator.Processors.Patch;
    ScreenProcessor = Grasppe.ConRes.PatchGenerator.Processors.Screen;
    PrintProcessor  = Grasppe.ConRes.PatchGenerator.Processors.Print;
    ScanProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Scan;
    View
  end
  
  methods
    
    function obj = PatchGeneratorProcessor()
      obj = obj@Grasppe.Occam.Process;
      obj.permanent = true;
      obj.addProcess(obj.PatchProcessor);
      obj.addProcess(obj.ScreenProcessor);
      obj.addProcess(obj.PrintProcessor);
      obj.addProcess(obj.ScanProcessor);
    end
    
    function output = Run(obj)
      output = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      
      patchProcessor  = obj.PatchProcessor;
      screenProcessor = obj.ScreenProcessor;
      printProcessor  = obj.PrintProcessor;
      scanProcessor   = obj.ScanProcessor;
      
      parameters      = obj.Parameters;
      
      
      % try screenProcessor.Parameters = parameters.Screen; end
      try printProcessor.Parameters = parameters.Print; end
      try scanProcessor.Parameters = parameters.Scan; end
      
      % ffScreening( contone, resolution, title, spi, lpi, dpi, angles, noise)
      
      try
        %         % Screening
        %         addressability  = findField(parameters.Screen, 'addressability');
        %         screenlpi       = findField(parameters.Screen, 'resolution');
        %         screenangle     = findField(parameters.Screen, 'angle');
        
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
        
      end
      
      try
      
        %% Generate Patch
        patchProcessor.Execute(parameters.Patch);
        patchImage = output.Image;
        
        
        %% Screen & Print Patch
        parameters.Screen.PrintProcessor  = printProcessor;
        parameters.Screen.PixelResolution = patchProcessor.Addressability;
        parameters.Screen.PrintParameters = parameters.Print;
        
        screenProcessor.Execute(parameters.Screen);
        screenedImage = output.Image;
        
        %% Scan Patch
        scanProcessor.Execute(parameters.Scan);
        scannedImage = output.Image;
        
      
        
        % [img s] = Grasppe.Kit.ConRes.GeneratePatchImage(reference, contrast, resolution); %, width, addressability);
        
        % patchImage    = im2double(img);
        %screenedImage = grasppeScreen3(patchImage, scanpdi, addressability, screenlpi, screenangle );
%         printedImage  = im2double(screenedImage);
%         scannedImage  = im2double(printedImage);
        
        % imagePath, ppi, spi, lpi, angle
        
        %subplot(2,3,[1 2 4 5]);
        
        s = warning('off', 'Images:initSize:adjustingMag');

        % clf;
        % hIm = imshow(scannedImage); % ,'InitialMagnification', 100, 'Border', 'loose');
        % hSP = imscrollpanel(gcf,hIm);
        % api = iptgetapi(hSP);
        % api.setMagnification(1); % 2X = 20
        
        obj.View.setImage(scannedImage);
        
        %output = scannedImage;
        
        % figure, imshow(scannedImage);
        
        warning(s);
        
        set(gcf,'Name',toString(s));
      end
      drawnow expose update;
      % imshow(imfilter(im2double(grasppeScreen3(img, addressability)), H),'InitialMagnification', 100); set(gcf,'Name',toString(s)); drawnow expose update;
    end
  end
  
end

