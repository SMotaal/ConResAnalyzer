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
    
    
    function output = RunSeries(obj, varargin) %CRange, RRange)
      
      delete(timerfindall);
      
      obj.UpdateProgressComponents;
      
      %jProgressBar = javax.swing.JProgressBar;
      %jProgressBar.setIndeterminate(true);
      %[jhProgressBar, hContainer] = javacomponent(jProgressBar,[20,20,200,40]);
      
      %stages    = [3 10];
      
      %progress  = 0;
      %stage     = 5;
      
      drawnow expose update;
      
      try
        
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
        
        runData = [];
        
        prefix        = fullfile('Output', 'series-');
        
        imagefiles = {};
        
        %for rvalue = RRange
        %for cvalue = CRange
        
        SRange = varargin;
        
        if numel(SRange)==1
          SRange = SRange{1};
        elseif numel(SRange)==2
          SRange = [50, SRange];
        elseif numel(SRange)==3
          trange = SRange{1};
          crange = SRange{2};
          rrange = SRange{3};
          
          newRange = zeros(numel(trange)*numel(crange)*numel(rrange),3);
          m = 1;
          for t = trange
            for r = rrange
              for c = crange
                newRange(m,:) = [t c r];
                m = m +1;
              end
            end
          end
          
          SRange = newRange;
        end
        
        if ~iscell(SRange) && isnumeric(SRange)
          newRange = cell(1,size(SRange,1));
          
          for s = 1:size(SRange,1)
            newRange{s} = SRange(s, :);
          end
          
          SRange = newRange;
        end
        
        ms2 = 1/numel(SRange);
        
        for s = 1:numel(SRange)
          
          % jProgressBar.setIndeterminate(true);
          
          svalue = SRange{s};
          tvalue = svalue(1);
          cvalue = svalue(2);
          rvalue = svalue(3);
          
          output                    = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
          % try screenProcessor.Parameters = parameters.Screen; end
          printProcessor.Parameters = parameters.Print;
          scanProcessor.Parameters  = parameters.Scan;
          
          parameters.Patch.Mean       = tvalue;
          parameters.Patch.Contrast   = cvalue;
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
            
            output.Variables.HumanFactor    = (output.Variables.ScanFactor/25.4) *0.3;
            
            
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
            
            suffix        = [ ...
              '-V' num2str(round(RTV),    '%0.3d') ...
              '-R' num2str(round(RES*100),'%0.3d') ...
              '-C' num2str(round(CON),    '%0.3d') ...
              '-F' num2str(round(FQ*10),  '%0.3d')];
            
            
            
            %           filename       = fullfile('Output', ['series' ...
            %             '-V' num2str(round(RTV),    '%0.3d') ...
            %             '-R' num2str(round(RES*100),'%0.3d') ...
            %             '-C' num2str(round(CON),    '%0.3d') ...
            %             '-F' num2str(round(FQ*10),  '%0.3d')]);
            processImages = {screen, patch, reference, hiContrast};
            
            
            fxImages{1} = patch.copy;
            fxFFT{1} = crossRFFT(screen.Image, hiContrast.Image);
            fxImages{1}.Image = fxFFT{1}; %crossRFFT(screen.Image, hiContrast.Image);
            
            fxImages{2} = patch.copy;
            fxFFT{2} = crossRFFT(patch.Image, hiContrast.Image);
            fxImages{2}.Image = fxFFT{2}; %crossRFFT(patch.Image, hiContrast.Image);
            
            fxImages{3} = patch.copy;
            fxFFT{3} = crossRFFT(reference.Image, hiContrast.Image);
            fxImages{3}.Image = fxFFT{3}; %crossRFFT(reference.Image, hiContrast.Image);
            
            fxImages{4} = patch.copy;
            fxFFT{4} = crossRFFT(hiContrast.Image, hiContrast.Image);
            fxImages{4}.Image = fxFFT{4}; %crossRFFT(hiContrast.Image, hiContrast.Image);
            
            seriesData  = [];
            seriesRData  = [];
            seriesCData  = [];
            seriesImage = [];
            seriesRmg   = [];
            seriesCmg = [];
            
            seriesFqs = {};
            seriesRFqs = {};
            seriesCFqs = {};
            seriesIms = {};
            seriesRms = {};
            seriesCms = {};
            
            dataColumn = 4;
            
            %jProgressBar.setIndeterminate(false);
            
            %progress = progress+stage;
            %stage     = 40*ms2;
            
            %ms = numel(processImages);
            %qprogress = progress;
            
            parfor m = 1:numel(processImages)
              processImage = processImages{m};
              
              data    = processImage.Fourier;
              image   = processImage.Image;
              
              rmage   = retina(image);
              
              rdata   = fFFT(rmage);
              
              [bFq fqData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(data));
              
              [bFq fqRData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(rdata));
              
              seriesFqs{m} = fqData(:,dataColumn);
              seriesRFqs{m} = fqRData(:,dataColumn);
              seriesIms{m} = image;
              seriesRms{m} = rmage; %retina(image);
              
              %qprogress = jProgressBar.getValue + stage/ms;
              %jProgressBar.setValue(qprogress);
              
            end
            
            %progress = qprogress;
            
            %jProgressBar.setValue(progress + stage);
            
            %progress = progress+stage;
            %stage     = 40*ms2;
            
            %ms = numel(fxImages);
            %qprogress = progress;
            parfor m = 1:numel(fxImages)
              fxImage = fxImages{m};
              
              cdata   = fxFFT{m}; %fxImage.Fourier;
              %cmage   = levelFFT(cdata); %fxImage.FourierImage;
              
              [bFq fqCData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(cdata));
              
              seriesCFqs{m} = fqCData(:,dataColumn);
              %seriesCms{m}  = cmage; %retina(image);
              
              %jProgressBar.setValue(progress + stage * m/ms); %setString([int2str(round(progress + stage * m/ms)) '%']);
              %qprogress = jProgressBar.getValue + stage/ms;
              %jProgressBar.setValue(qprogress);             
              %jProgressBar.setIndeterminate(false);
            end
            
            % progress = qprogress;
            
            %jProgressBar.setValue(progress + stage);
            
            % jProgressBar.setIndeterminate(true);
                        
            seriesData = [1:size(seriesFqs{1},1)]';
            for m = 1:numel(processImages)
              seriesData  = [seriesData seriesRFqs{m}]; % seriesFqs{m}];
              seriesImage = [seriesImage seriesIms{m}];
              seriesRmg   = [seriesRmg seriesRms{m}];
            end
            
            %seriesData = [seriesData(:,1) seriesData(:,2:2:end) seriesData(:,3:2:end)];
            
            for m = 1:numel(fxImages)
              seriesData  = [seriesData seriesCFqs{m}];
              %seriesCmg   = [seriesCmg seriesCms{m}];
            end
            
            
            seriesImage = [seriesImage; seriesRmg];
            
            zv = seriesData([-1:1]+floor(FQ),:);
            [zc zi] = max(zv(:,end)); % yR(ceil(m))]);
            zv = zv(zi,:);
            zv(1) = FQ;
            
            runData = [runData; [RTV CON RES zv]];
            
            imagefile   = [prefix 'image' suffix '.png'];
            
            imagefiles{end+1} = imagefile;
            
            seriesData  = [zv; seriesData];
            
            %stage     = 5*ms2;
            %progress = progress+stage;
            %jProgressBar.setValue(progress);
            
            dlmwrite([prefix 'data' suffix '.txt'], seriesData, '\t');
            imwrite(seriesImage, imagefile); %[prefix 'image' suffix '.png']);
            
            %stage     = 5*ms2;
            %progress = progress+stage;
            %jProgressBar.setValue(progress);
            %imwrite(seriesCmg, [prefix 'fftcr' suffix '.png']);
            %imwrite(seriesCmg, [filename '-cross.png']);
            
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
          
          %end
        end
        
        %summaryData = runData;
        
        jProgressBar.setValue(99);
        jProgressBar.setIndeterminate(false);
        
        lData = runData(:, 9);
        aData = runData(:, 10);
        rData = runData(:, 11);
        zData = runData(:, 12);
        
        lzData = lData ./ zData;
        azData = aData ./ zData;
        rzData = rData ./ zData;
        
        arData = aData ./ rData;
        
        summaryData = [runData lzData azData rzData arData];
        
        dlmwrite([prefix 'data-summary.txt'], runData, '\t');
        
        htmlcode{1} = ['<html>' ...
          '<style>' ...
          'table {border: 1px solid; border-collapse: collapse} ' ...
          'td, th {border: 1px solid; width: 3%; text-align: center}  ' ...
          'td:nth-of-type(odd), th:nth-of-type(odd) {background-color: #aaa;} ' ...
          'img {height: 175 px; width: auto;} ' ...
          '</style>' ...
          '<table>'];
        
        
        htmlcode{2} = ['<thead><tr>' ...
          '<th>Image</th>' ...
          '<th>T</th> <th>C</th> <th>R</th> <th>F</th>' ...
          '<th>S</th> <th>P</th> <th>C</th> <th>H</th>' ...
          '<th>SH</th> <th>PH</th> <th>CH</th> <th>HH</th>' ...
          ... % '<th>SH/HH</th> <th>PH/HH</th> <th>CH/HH</th> <th>PH/CH</th>' ...
          '</tr></thead>'];
        
        for m = 1:size(runData,1)
          imagefile = imagefiles{m};
          
          [pth fname ext] = fileparts(imagefile);
          
          imagefile = [fname ext];
          
          %data = summaryData(m,:);
          
          imgcode   = sprintf('<td><img src="%s" /></td>', imagefile);
          
          datacode  = sprintf([ ...
            '<td>%0.0f</td><td>%0.0f</td><td>%4.3f</td><td>%2.1f</td>' ...
            '<td>%3.3f</td><td>%3.3f</td><td>%3.3f</td><td>%3.3f</td>' ...
            '<td>%3.3f</td><td>%3.3f</td><td>%3.3f</td><td>%3.3f</td>' ...
            ... % '<td>%3.3f</td><td>%3.3f</td><td>%3.3f</td><td>%3.3f</td>' ...
            ], ...
            runData(m,:) ... %runData(m,1:4), runData(m,9:12), lData, aData, rData, zData, ...
            ... %lzData(m), azData(m), rzData(m), arData(m) ...
            );
          
          rowcode   = ['<tr>' imgcode datacode '</tr>'];
          
          htmlcode{end+1} = rowcode;
        end
        
        htmlcode{end+1} = '</table></html>';
        
        dlmwrite([prefix 'data-summary.html'], strvcat(htmlcode),'');
        
        uiopen([prefix 'data-summary.html'],1);
        
        parameters.Patch.Contrast     = csetting;
        parameters.Patch.Resolution   = rsetting;
        
      end
      delete(jhProgressBar);
      drawnow expose update;
      
    end
    
    function output = Run(obj)
      
      %debugging = true;
      
      delete(timerfindall);
      
      obj.UpdateProgressComponents;
      
      prepTasks = obj.ProcessProgress.addTask('Prepare Process Components', 4);
      
      % procTasks = obj.addTask('Executing Processes', 4);
      
      %% varTask Estimation
      processors  =   obj.Parameters.Processors;
      
      n = numel(fieldnames(processors));
      
      n = n + 2*(n-5) + 2;  % Patch Rendering
      
      varTasks  = obj.ProcessProgress.addTask('Executing Processes', n);
      
      
      %jProgressBar = javax.swing.JProgressBar;
      %jProgressBar.setIndeterminate(true);
      %[jhProgressBar, hContainer] = javacomponent(jProgressBar,[20,20,200,40]);      
      
      panel       = obj.View;
      panel.clearAxes();
      
      drawnow expose update;
      
      CHECK(prepTasks); % 1
      
      try
        
        evalin('base', 'clear BandIntensityData;');
        
        assignin('base', 'BandIDX', 0);
        
        output      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        reference   = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        % hireference = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        patch       = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        screen      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        
        CHECK(prepTasks); % 2
        
        patchProcessor    = obj.PatchProcessor;
        screenProcessor   = obj.ScreenProcessor;
        printProcessor    = obj.PrintProcessor;
        scanProcessor     = obj.ScanProcessor;
        
        CHECK(prepTasks); % 3
        
        parameters                = obj.Parameters;

        printProcessor.Parameters = parameters.Print;
        scanProcessor.Parameters  = parameters.Scan;
        
        CHECK(prepTasks); % 4
        
        SEAL(prepTasks); % 4
        
        try
                    
          %% Generate Patch
          patchProcessor.Execute(parameters.Patch);
          patchImage = output.Image;
          
          CHECK(varTasks); % 1
          
          %% Screen & Print Patch
          parameters.Screen.PrintProcessor  = printProcessor;
          parameters.Screen.PixelResolution = patchProcessor.Addressability;
          parameters.Screen.PrintParameters = parameters.Print;
          
          screenProcessor.Execute(parameters.Screen);
          
          screenedImage = output.Image;
          screen        = screenProcessor.HalftoneImage;
          screenImage   = screen.Image;
          
          CHECK(varTasks); % 2
          
          %% Scan Patch
          scanProcessor.Execute(parameters.Scan);
          scannedImage = output.Image;
          patch.setImage(scannedImage, output.Resolution);
          
          CHECK(varTasks); % 3
          
          %% Patch & Reference Images
          
          referenceImage  = imresize(patchImage,  size(scannedImage));
          reference.setImage(referenceImage, output.Resolution);
          
          CHECK(varTasks); % 4
          
          screenImage     = imresize(screenImage, size(scannedImage));
          screen.setImage(screenImage, output.Resolution);
          
          CHECK(varTasks); % 5
                    
          output.Variables.PatchImage     = patch;
          output.Variables.ReferenceImage = reference;
          output.Variables.HalftoneImage  = screen;
          
          %parameters.Scan.Resolution
          
          output.Variables.ScanFactor     = parameters.Scan.Resolution*parameters.Scan.Scale/100;
          
          output.Variables.HumanFactor    = (output.Variables.ScanFactor/25.4) *0.3;
          
        catch err
          disp(err);
        end
        
        output = copy(output);
        output.Snap('GeneratedPatch');
        
        CHECK(procTasks); % 1
        
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
          
          CHECK(varTasks); % 5 + n
          
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
          
          CHECK(procTasks); % 2
          
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
            
            CHECK(varTasks); % 5 + n1 + n
          end
          
          SEAL(varTasks);
          
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
          
          CHECK(procTasks); % 3
        catch err
          disp(err);
        end
        
        drawnow expose update;
        
        CHECK(procTasks); % 4
        % imshow(imfilter(im2double(grasppeScreen3(img, addressability)), H),'InitialMagnification', 100); set(gcf,'Name',toString(s)); drawnow expose update;
        %catch err
      end
      %delete(jhProgressBar);
      drawnow expose update;
      
      SEAL(procTasks);
    end
    
  end
  
end

