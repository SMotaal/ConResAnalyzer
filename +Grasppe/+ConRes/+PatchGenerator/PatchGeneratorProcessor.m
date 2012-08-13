classdef PatchGeneratorProcessor < Grasppe.Occam.Process
  %PATCHGENERATORPROCESSOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    PatchProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Patch;
    ScreenProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Screen;
    PrintProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Print;
    ScanProcessor     = Grasppe.ConRes.PatchGenerator.Processors.Scan;
    UserProcessor     = Grasppe.ConRes.PatchGenerator.Processors.UserFunction;
    % DisplayProcessor  = Grasppe.ConRes.PatchGenerator.Processors.Display;
    View
  end
  
  methods
    
    function obj = PatchGeneratorProcessor()
      obj = obj@Grasppe.Occam.Process;
      %       obj.PatchProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Patch;
      %       obj.ScreenProcessor   = Grasppe.ConRes.PatchGenerator.Processors.Screen;
      %       obj.PrintProcessor    = Grasppe.ConRes.PatchGenerator.Processors.Print;
      %       obj.ScanProcessor     = Grasppe.ConRes.PatchGenerator.Processors.Scan;
      %       obj.UserProcessor     = Grasppe.ConRes.PatchGenerator.Processors.UserFunction;
      obj.permanent = true;
      obj.addProcess(obj.PatchProcessor);
      obj.addProcess(obj.ScreenProcessor);
      obj.addProcess(obj.PrintProcessor);
      obj.addProcess(obj.ScanProcessor);
      obj.addProcess(obj.UserProcessor);
      % obj.addProcess(obj.DisplayProcessor);
    end
    
    function addProcess(obj, process)
      obj.addProcess@Grasppe.Occam.Process(process);
      process.Controller  = obj;
      process.View        = obj.View;
    end
    
    
    function output = RunSeries(obj, CRange, RRange)
      
      delete(timerfindall);
      
      fxProcessor = obj.UserProcessor;
      
      fourier       = @(varargin) fxProcessor.fourier(varargin{:});
      forwardFFT    = @(varargin) fxProcessor.forwardFFT(varargin{:});
      inverseFFT    = @(varargin) fxProcessor.inverseFFT(varargin{:});
      
      fFFT          = @(varargin) forwardFFT(varargin{:});
      iFFT          = @(varargin) inverseFFT(varargin{:});
      
      display       = @(varargin) fxProcessor.display(varargin{:});
      
      add           = @(varargin) fxProcessor.algebra('add', varargin{:});
      subtract      = @(varargin) fxProcessor.algebra('subtract', varargin{:});
      multiply      = @(varargin) fxProcessor.algebra('multiply', varargin{:});
      divide        = @(varargin) fxProcessor.algebra('divide', varargin{:});
      
      sub           = @(varargin) subtract(varargin{:});
      mul           = @(varargin) multiply(varargin{:});
      div           = @(varargin) divide(varargin{:});
      
      normalize     = @(x)        x/nanmax(x(:));
      invert        = @(x)        1-im2double(x);
      binarize      = @(x,y)      im2bw(real(x),y);
      
      bin           = @(x,y)      binarize(x, y);
      nor           = @(x)        normalize(x);
      normaverse    = @(x)        invert(normalize(x));
      norverse      = @(x)        normaverse(x);
      binormalize   = @(x, y)     binarize(normalize(x),y);
      binor         = @(x, y)     binormalize(x,y);
      binormaverse  = @(x, y)     binarize(normaverse(x),y);
      binorverse    = @(x, y)     binormaverse(x,y);
      
      
      disk          = @(x, y)     imfilter(x,fspecial('disk',y),'replicate');
      
      HR            = 10;
      retina        = @(x)        disk(x, HR);
      
      retinaFFT     = @(x)        fFFT(retina(x));
      
      crossRFFT     = @(x, y)     mul(retinaFFT(x), retinaFFT(y));
      
      level         = @(varargin) imadjust(varargin{:});
      levelFFT      = @(varargin) fxProcessor.levelFFT(varargin{:});
      
      iFFTL         = @(varargin) level(inverseFFT(varargin{:}));
      
      logabs        = @(x)        log(abs(x));
      
      
      
      output      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      reference   = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      % hireference = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      patch       = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      screen      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      
      panel       = obj.View;
      
      patchProcessor    = obj.PatchProcessor;
      screenProcessor   = obj.ScreenProcessor;
      printProcessor    = obj.PrintProcessor;
      scanProcessor     = obj.ScanProcessor;
      
      parameters  = obj.Parameters;   
      
      csetting    = parameters.Patch.Contrast;
      rsetting    = parameters.Patch.Resolution;
      
      Tally = {'ID', 'TV', 'CV', 'RV', 'FQ', 'F-Sum', 'F-Area', 'F-Modulus', 'C-Modulus', 'Function'};
      
      ImageIndex = 0;
      
      % bands = 71;

      for rvalue = RRange
        for cvalue = CRange

          output      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
          % try screenProcessor.Parameters = parameters.Screen; end
          printProcessor.Parameters = parameters.Print;
          scanProcessor.Parameters = parameters.Scan;
          
          parameters.Patch.Contrast = cvalue;
          parameters.Patch.Resolution = rvalue;
          
          try
            
            %% Fix size
            % parameters.Patch.Size = parameters.Patch.Size/
            
            %% Generate Patch
            patchProcessor.Execute(parameters.Patch);
            patchImage = output.Image;
            
            
            %% Screen & Print Patch
            parameters.Screen.PrintProcessor  = printProcessor;
            parameters.Screen.PixelResolution = patchProcessor.Addressability;
            parameters.Screen.PrintParameters = parameters.Print;
            
            screenProcessor.Execute(parameters.Screen);
            
            screenedImage = output.Image;
            screen        = screenProcessor.HalftoneImage;
            screenImage   = screen.Image;
            
            %% Scan Patch
            scanProcessor.Execute(parameters.Scan);
            scannedImage = output.Image;
            
            %output.Snap('GeneratedPatch');
            
            %% Patch & Reference Images
            referenceImage  = imresize(patchImage,  size(scannedImage));
            screenImage     = imresize(screenImage, size(scannedImage));
            
            patch.setImage(scannedImage, output.Resolution);
            reference.setImage(referenceImage, output.Resolution);
            % hireference.setImage(imadjust(referenceImage), output.Resolution);
            screen.setImage(screenImage, output.Resolution);
            
            output.Variables.PatchImage     = patch;
            output.Variables.ReferenceImage = reference;
            output.Variables.HalftoneImage  = screen;
            
            %parameters.Scan.Resolution
            
            output.Variables.ScanFactor     = parameters.Scan.Resolution*parameters.Scan.Scale/100;
            
            output.Variables.HumanFactor    = (output.Variables.ScanFactor/25.4) *0.25;
            
            
            RES           = parameters.Patch.Resolution;
            CON           = parameters.Patch.Contrast;
            RTV           = parameters.Patch.Mean;
            
            SR            = output.Variables.ScanFactor;
            HR            = output.Variables.HumanFactor;
            
            PS            = ceil(parameters.Patch.Size*(SR/25.4));
            
            FQ            = RES / (SR/25.4) * PS * 2;
            
            
            hiContrast                      = reference.copy;
            hiContrast.Image                = imadjust(hiContrast.Image);
            
            imageIds      = {'S', 'P', 'R', 'C'};
            filename       = fullfile('Output', ['series' ...
              '-V' num2str(round(RTV),    '%0.3d') ...
              '-R' num2str(round(RES*100),'%0.3d') ...
              '-C' num2str(round(CON),    '%0.3d') ...
              '-F' num2str(round(FQ*10),  '%0.3d')]);
            processImages = {screen, patch, reference, hiContrast};
            
            seriesData  = [];
            seriesRData  = [];
            seriesImage = [];
            seriesRmg   = [];
            
            seriesFqs = {};
            seriesRFqs = {};
            seriesIms = {};
            seriesRms = {};
            
            dataColumn = 4;
            
            parfor m = 1:numel(processImages)
              processImage = processImages{m};
              
              data  = processImage.Fourier;
              image = processImage.Image;
              
              rmage = retina(image);
              
              rdata  = fFFT(rmage);
              
              [bFq fqData] = Grasppe.Kit.ConRes.CalculateBandIntensity(data);
              
              [bFq fqRData] = Grasppe.Kit.ConRes.CalculateBandIntensity(rdata);

              seriesFqs{m} = fqData(:,dataColumn);
              seriesRFqs{m} = fqRData(:,dataColumn);
              seriesIms{m} = image;
              seriesRms{m} = rmage; %retina(image);
            end
            
            seriesData = [1:size(seriesFqs{1},1)]';
            for m = 1:numel(processImages)
              seriesData  = [seriesData seriesRFqs{m} seriesFqs{m}];
              seriesImage = [seriesImage seriesIms{m}];
              seriesRmg   = [seriesRmg seriesRms{m}];
            end
            
            seriesData = [seriesData(:,1) seriesData(:,2:2:end) seriesData(:,3:2:end)];
            
            seriesImage = [seriesImage; seriesRmg];
            
            dlmwrite([filename '.txt'], seriesData, '\t');
            imwrite(seriesImage, [filename '.png']);

            %imwrite(, [filename '.png']);
            
          catch err
            disp(err);
          end
          
%           try
%             %% Functions
%             
%             fxNames     = fieldnames(parameters.Processors);
%             fxProcessor = obj.UserProcessor;
%             
%             for fxName = fxNames'
%               fx = parameters.Processors.(char(fxName));
%               fxProcessor.Parameters = fx;
%               fxProcessor.Execute;
%               
%               % disp(fxProcessor.Output.Variables);
%               
%               %disp(fx);
%               id = char(fxName);
%               try id = char(fx.Expression); end
%               
%               try
%                 % output.Snap(id);
%               catch err
%                 disp(err)
%               end
%             end
%             
%           end
          
%           tRow = size(Tally,1)+1;
%           
%           Tally(tRow:tRow+3, :) = output.Variables.Tally(2:end,:);
%           
%           ImageIndex = ImageIndex + 1;
%           imwrite(scannedImage, ['Output/image' int2str(ImageIndex) '.png']);
          
        end
      end
      
      %mat2clip(Tally);
      
      parameters.Patch.Contrast     = csetting;
      parameters.Patch.Resolution   = rsetting;
      
    end
    
    function output = Run(obj)
      
      %debugging = true;
      
      delete(timerfindall);
      
      evalin('base', 'clear BandIntensityData;');
      
      assignin('base', 'BandIDX', 0);
      
      output      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      reference   = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      % hireference = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      patch       = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      screen      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
      
      panel       = obj.View;
      
      patchProcessor    = obj.PatchProcessor;
      screenProcessor   = obj.ScreenProcessor;
      printProcessor    = obj.PrintProcessor;
      scanProcessor     = obj.ScanProcessor;
      
      parameters      = obj.Parameters;
      
      
      % try screenProcessor.Parameters = parameters.Screen; end
      printProcessor.Parameters = parameters.Print;
      scanProcessor.Parameters = parameters.Scan;
      
      % ffScreening( contone, resolution, title, spi, lpi, dpi, angles, noise)
      
      % try
      %   % Printing
      %   tvi             = findField(parameters.Print, 'gain');
      %   noise           = findField(parameters.Print, 'noise');
      %   blur            = findField(parameters.Print, 'blur');
      %   spread          = findField(parameters.Print, 'radius');
      %
      %   % Scanning
      %   scanpdi         = findField(parameters.Scan, 'resolution');
      %   scanscale       = findField(parameters.Scan, 'scale');
      %
      %
      %   % Overloads
      %   width           = 256;
      % end
      
      panel.clearAxes();
      
      try
        
        %% Fix size
        % parameters.Patch.Size = parameters.Patch.Size/
        
        %% Generate Patch
        patchProcessor.Execute(parameters.Patch);
        patchImage = output.Image;
        
        
        %% Screen & Print Patch
        parameters.Screen.PrintProcessor  = printProcessor;
        parameters.Screen.PixelResolution = patchProcessor.Addressability;
        parameters.Screen.PrintParameters = parameters.Print;
        
        screenProcessor.Execute(parameters.Screen);
        
        screenedImage = output.Image;
        screen        = screenProcessor.HalftoneImage;
        screenImage   = screen.Image;
        
        %% Scan Patch
        scanProcessor.Execute(parameters.Scan);
        scannedImage = output.Image;
        
        %output.Snap('GeneratedPatch');
        
        %% Patch & Reference Images
        referenceImage  = imresize(patchImage,  size(scannedImage));
        screenImage     = imresize(screenImage, size(scannedImage));
        
        patch.setImage(scannedImage, output.Resolution);
        reference.setImage(referenceImage, output.Resolution);
        % hireference.setImage(imadjust(referenceImage), output.Resolution);
        screen.setImage(screenImage, output.Resolution);
        
        output.Variables.PatchImage     = patch;
        output.Variables.ReferenceImage = reference;
        output.Variables.HalftoneImage  = screen;
        
        %parameters.Scan.Resolution
        
        output.Variables.ScanFactor     = parameters.Scan.Resolution*parameters.Scan.Scale/100;
        
        output.Variables.HumanFactor    = (output.Variables.ScanFactor/25.4) *0.25;

      catch err
        disp(err);
      end
      
      output = copy(output);
      output.Snap('GeneratedPatch');
      
      try
        %% Functions
        
        fxNames     = fieldnames(parameters.Processors);
        fxProcessor = obj.UserProcessor;
        
        for fxName = fxNames'
          fx = parameters.Processors.(char(fxName));
          fxProcessor.Parameters = fx;
          fxProcessor.Execute;
          
          dispdbg(fxProcessor.Output.Variables);
          
          dispdbg(fx);
          id = char(fxName);
          try id = char(fx.Expression); end
          
          try
            output.Snap(id);
          catch err
            disp(err)
          end
        end
        
      end
      
      
      try
        
        images  = output.Snapshots(:,1);
        ids     = output.Snapshots(:,2);
        
        images{end+1} = output;
        ids{end+1}    = 'FinalOutput';
        
        % images{end+1} = output.Fourier;
        % ids{end+1}    = 'FinalFFT';
        
        tOp0  = {'Units', 'normalized', ...
          'HorizontalAlignment', 'left', ...
          'VerticalAlignment', 'top', ...
          'BackgroundColor', [0 0 0], 'Color', [1 1 1], ...
          'FontSize', 7, 'FontName', 'DIN', 'FontUnits', 'pixels'};
        
        tXY   = {0.01, 0.99};
        tXY2  = {0.01, 0.01};
        tOp   = {tOp0{:}};
        
        tOp2   = {tOp0{:}, 'VerticalAlignment', 'bottom'};
        
        
        tSize = @(x) [int2str(size(x,1)), 'x', ...
          int2str(size(x,2)), 'x', ...
          int2str(size(x,3))];
        
        
        for m = 2:numel(images)-1
          snapshot = images{m};
          image = snapshot.Image;
          %snapshot.generatePlotFFT();
          fftimage = snapshot.FourierImage;
          
          fftMode = ~isreal(image);
          
          if fftMode
                        
            invimg = output.inverseFFT(image);
            panel.setImage(invimg);
            
            try
              T   = [ids{m} ' inv'];
              text(tXY{:}, T, 'Parent', gca, tOp{:});
              text(tXY2{:}, tSize(invimg), 'Parent', gca, tOp2{:});
            end
            
            %bandImage = output.bandPlotFFT(
            
            %             image     = real(log(1+abs(image)));
            %             imageMin  = min(image(:)); imageMax = max(image(:));
            %             image     = (image-imageMin) / (imageMax-imageMin);
            
            image = fftimage; %snapshot.FourierImage; %.bandPlotFFT(image);
            
          end
          panel.setImage(image);
          
          try
            T   = [ids{m}];
            text(tXY{:}, T, 'Parent', gca, tOp{:});
            text(tXY2{:}, tSize(image), 'Parent', gca, tOp2{:});
          end
          
          
          if ~fftMode
            image     = fftimage; %snapshot.FourierImage; %output.bandPlotFFT(output.forwardFFT(image));
            %image     = real(log(1+abs(image)));
            %imageMin  = min(image(:)); imageMax = max(image(:));
            %image     = (image-imageMin) / (imageMax-imageMin);
            
            panel.setImage(fftimage); %output.inverseFFT(image));
            
            try
              T   = [ids{m} ' fwd'];
              text(tXY{:}, T, 'Parent', gca, tOp{:});
              text(tXY2{:}, tSize(image), 'Parent', gca, tOp2{:});
            end
          end
          %ti(ids{m});
        end
        
        panel.layoutAxes;
        %         image = output.Image;
        %
        %
        %         if isequal(output.Domain,'frequency')
        %           image     = real(log(1+abs(image)));
        %           imageMin  = min(image(:)); imageMax = max(image(:));
        %           image     = (image-imageMin) / (imageMax-imageMin);
        %         end
        %elseif output.Domain = 'spatial'
        
        %panel.setImage(image);
        %         panel.setImage(image);
        %         panel.setImage(image);
        
        set(gcf,'Name','ConRes');
      catch err
        disp(err);
      end
      
      drawnow expose update;
      % imshow(imfilter(im2double(grasppeScreen3(img, addressability)), H),'InitialMagnification', 100); set(gcf,'Name',toString(s)); drawnow expose update;
    end
  end
  
end

