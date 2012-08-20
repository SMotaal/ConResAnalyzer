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

      obj.permanent = true;
      obj.addProcess(obj.PatchProcessor);
      obj.addProcess(obj.ScreenProcessor);
      obj.addProcess(obj.PrintProcessor);
      obj.addProcess(obj.ScanProcessor);
      obj.addProcess(obj.UserProcessor);
    end
    
    function addProcess(obj, process)
      obj.addProcess@Grasppe.Occam.Process(process);
      process.Controller  = obj;
      process.View        = obj.View;
    end
    
    
    function output = RunSeries(obj, varargin)
      
      delete(timerfindall);
      
      clc;
      
      %% Progress Initialization
      obj.UpdateProgressComponents;
      
      obj.ProcessProgress.Window = obj.View.Window;
      
      obj.ProcessProgress.resetTasks;
      
      %% Prep Tasks Load Estimation
      obj.ProcessProgress.Maximum = 25;
      
      prepTasks = obj.ProcessProgress.addTask('Prepare Process', 3);
      
      obj.ProcessProgress.activateTask(prepTasks);
            
      drawnow expose update;
      
      CHECK(prepTasks); %1
      
      try
        
        conresfx;
        
        output            = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        reference         = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        patch             = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        screen            = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
        
        panel             = obj.View;
        
        patchProcessor    = obj.PatchProcessor;
        screenProcessor   = obj.ScreenProcessor;
        printProcessor    = obj.PrintProcessor;
        scanProcessor     = obj.ScanProcessor;
        
        parameters        = obj.Parameters;
        
        tsetting          = parameters.Patch.Mean;
        csetting          = parameters.Patch.Contrast;
        rsetting          = parameters.Patch.Resolution;
        
        % Tally = {'ID', 'TV', 'CV', 'RV', 'FQ', 'F-Sum', 'F-Area', 'F-Modulus', 'C-Modulus', 'Function'};
        
        CHECK(prepTasks); %2
        
        runData     = [];
        imagefiles  = {};
        hifiles  = {};
        scrfiles = {};
        
        outset      = 'data';
        outseries   = ['Series-' num2str(3,'%03d')];        
        outpath     = fullfile('Output');
        
        SRange = varargin;
        
        if numel(SRange)==1
          SRange = SRange{1};
        elseif numel(SRange)==2
          SRange = [50, SRange];
        elseif numel(SRange)==3
          trange = SRange{1};
          crange = SRange{2};
          rrange = SRange{3};
          
          if numel(trange)==1 && numel(crange)~=1 && numel(rrange)~=1
              outset      = ['rtv-' num2str(trange,'%03d')];            
              outpath     = fullfile('Output', outseries, ['RTV-' int2str(trange)]);
              dispf('Exporting Series: Reference Tone Value %2.1f%%', trange);
          elseif numel(crange)==1 && numel(trange)~=1 && numel(rrange)~=1
              outset      = ['con-' num2str(crange,'%03d')];            
              outpath     = fullfile('Output', outseries, ['CON-' int2str(trange)]);
              dispf('Exporting Series: Contrast Value %2.1f%%', crange);
          elseif numel(rrange)==1 && numel(crange)~=1 && numel(trange)~=1
              outset      = ['res-' num2str(rrange*100,'%03d')];            
              outpath     = fullfile('Output', outseries, ['RES-' int2str(trange)]);
              dispf('Exporting Series: Resolution Value %3.2f lp/mm', rrange);
          end            
          
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
        
        prefix      = fullfile(outpath , [lower(outseries) '-']);
        
        try mkdir(outpath); end
        
        %% Fixed Tasks Load Estimation
        procTasks = obj.ProcessProgress.addTask('Executing Processes', 7);
        
        %% Variable Tasks Load Estimation
        nV        = 10;
        nI        = numel(SRange); %numel(fieldnames(obj.Parameters.Processors));
        nT        = nI*nV;  % Patch Rendering
        
        varTasks  = obj.ProcessProgress.addTask('Rendering', nT);
        
        obj.ProcessProgress.Maximum = [];
        prepTasks.Factor = round(min(1, 10/3));
        SEAL(prepTasks); %3        
        
        obj.ProcessProgress.activateTask(varTasks);
        
        CHECK(procTasks); % 1
        
        strSettings     = {'RTV', 'CON', 'RES'};
        numSettings     = numel(strSettings);
        runSettings     = [0, 0, 0];
        
        fxFFT           = cell(1,4);
        fxImages        = cell(1,4);
        sharpImages     = cell(1,4);
        
        screen          = [];
        hiContrast      = [];
        
        sharpSpectra    = cell(1,4);
        sharpPixels     = cell(1,4);
        retinaSpectra   = cell(1,4);
        retinaPixels    = cell(1,4);
        fxSpectra       = cell(1,4);
        fxPixels        = cell(1,4);
        
        for s = 1:numel(SRange)
          
          nv      = nV;
          
          svalue  = SRange{s};
          tvalue  = svalue(1);
          cvalue  = svalue(2);
          rvalue  = svalue(3);
          
          if ~isnumeric(runSettings) || numel(runSettings)~=numSettings
            currentSettings = zeros(1, numSettings);
          end
          
          newRTV  = ~isequal(runSettings(1), tvalue);
          newCON  = ~isequal(runSettings(2), cvalue);
          newRES  = ~isequal(runSettings(3), rvalue);
          
          if newRTV || newRES
            patchRange                = 1:4;
          else
            patchRange                = 2:3;
          end          
          
          if tvalue+(cvalue)>100 || tvalue-(cvalue/2)<0          
            CHECK(varTasks, nv); % n(v)
            continue;
          end
          
          output                      = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
          printProcessor.Parameters   = parameters.Print;
          scanProcessor.Parameters    = parameters.Scan;
          
          parameters.Patch.Mean       = tvalue;
          parameters.Patch.Contrast   = cvalue;
          parameters.Patch.Resolution = rvalue;
          
          try
                        
            %% Generate Patch
            patchProcessor.Execute(parameters.Patch);
            patchImage = output.Image;
            
            CHECK(varTasks); nv = nv -1; %1
            
            %% Screen & Print Patch
            parameters.Screen.PrintProcessor  = printProcessor;
            parameters.Screen.PixelResolution = patchProcessor.Addressability;
            parameters.Screen.PrintParameters = parameters.Print;
            
            screenProcessor.Execute(parameters.Screen);
            
            screenedImage   = output.Image;
            
            if newRTV || newRES || ~isscalar(screen) || ~isobject(screen) || ~isvalid(screen)
              screen        = screenProcessor.HalftoneImage;
            end            
            screenImage     = screen.Image;
            
            CHECK(varTasks); nv = nv -1; %2
            
            %% Scan Patch
            scanProcessor.Execute(parameters.Scan);
            scannedImage    = output.Image;
            
            CHECK(varTasks); nv = nv -1; %3
            
            %% Patch & Reference Images
            referenceImage  = imresize(patchImage,  size(scannedImage));
            screenImage     = imresize(screenImage, size(scannedImage));
            
            patch.setImage(scannedImage, output.Resolution);
            reference.setImage(referenceImage, output.Resolution);
            screen.setImage(screenImage, output.Resolution);
            
            output.Variables.PatchImage     = patch;
            output.Variables.ReferenceImage = reference;
            output.Variables.HalftoneImage  = screen;
            
            output.Variables.ScanFactor     = parameters.Scan.Resolution*parameters.Scan.Scale/100;
            output.Variables.HumanFactor    = (output.Variables.ScanFactor/25.4) *0.3;
            
            CHECK(varTasks); nv = nv -1; %4
            
            %% Retina Images
            
            RES           = parameters.Patch.Resolution;
            CON           = parameters.Patch.Contrast;
            RTV           = parameters.Patch.Mean;
            SR            = output.Variables.ScanFactor;
            HR            = output.Variables.HumanFactor;
            PS            = ceil(parameters.Patch.Size*(SR/25.4));
            FQ            = RES / (SR/25.4) * PS * 2;
            
            if newRTV || newRES || ~isscalar(hiContrast) || ~isobject(hiContrast) || ~isvalid(hiContrast)
              hiContrast                      = reference.copy;
              hiContrast.Image                = imadjust(hiContrast.Image);
            end
            % end
            
            imageIds      = {'SC', 'AP', 'CT', 'HC'};
            
            suffix        = [ ...
              '-V' num2str(round(RTV),    '%0.3d') ...
              '-R' num2str(round(RES*100),'%0.3d') ...
              '-C' num2str(round(CON*10), '%0.4d') ...
              '-F' num2str(round(FQ*10),  '%0.3d')];
            
            hisuffix        = [ ...
              '-V' num2str(round(RTV),    '%0.3d') ...
              '-R' num2str(round(RES*100),'%0.3d') ...
              '-F' num2str(round(FQ*10),  '%0.3d')];
            
            scrsuffix = hisuffix;
            
%             if newRTV || newRES
%               sharpImages{1}  = screen;
%               sharpImages{4}  = hiContrast;
%             end

            fxFFT{1}            = crossRFFT(screen.Image, hiContrast.Image);      % fxImages{1}.Image = fxFFT{1};
            fxFFT{4}            = crossRFFT(hiContrast.Image, hiContrast.Image);  % fxImages{4}.Image = fxFFT{4};
            fxFFT{2}            = crossRFFT(patch.Image, hiContrast.Image);       % fxImages{2}.Image = fxFFT{2};
            fxFFT{3}            = crossRFFT(reference.Image, hiContrast.Image);   % fxImages{3}.Image = fxFFT{3};
            
            for m = patchRange
              try delete(fxImages{m}); end
              fxImages{m}       = patch.copy;
              fxImages{m}.Image = fxFFT{m};
            end
            
            CHECK(varTasks); nv = nv -1; %5
            
            %% Image Processing
            
            seriesData    = [];
            seriesRData   = [];
            seriesCData   = [];
            seriesImage   = [];
            seriesRmg     = [];
            seriesCmg     = [];
            
            seriesFqs     = {};
            seriesRFqs    = {};
            seriesCFqs    = {};
            seriesIms     = {};
            seriesRms     = {};
            seriesCms     = {};
            
            dataColumn = 4;
            
            fFFT    = @(varargin) forwardFFT(varargin{:});
            retina  = @(x) disk(x, HR);
            
            %% Patch Images Processing  
            
            
            sharpImages = {screen, patch, reference, hiContrast};
            
            parfor m = 1:numel(sharpImages)
              processImage = sharpImages{m};
              
              if any(patchRange==m)
                data    = processImage.Fourier;
                image   = processImage.Image;
                rmage   = retina(image);
                rdata   = fFFT(rmage);

                [bFq fqData]  = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(data));
                [bFq fqRData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(rdata));
                
                sharpSpectra{m}   = fqData(:,dataColumn);
                retinaSpectra{m}  = fqRData(:,dataColumn);
                sharpPixels{m}    = image;
                retinaPixels{m}   = rmage;
              end

              seriesFqs{m}    = sharpSpectra{m}; % fqData(:,dataColumn);
              seriesRFqs{m}   = retinaSpectra{m}; %fqRData(:,dataColumn);
              seriesIms{m}    = sharpPixels{m}; % image;
              seriesRms{m}    = retinaPixels{m}; %rmage;
            end
            
            CHECK(varTasks); nv = nv -1; %6
            
            %% Retina Images Processing            
            
            parfor m = 1:numel(fxImages)
              if any(patchRange==m)
                fxImage   = fxImages{m};
                cdata     = fxFFT{m};
              %cmage   = levelFFT(cdata);
              
                [bFq fqCData] = Grasppe.Kit.ConRes.CalculateBandIntensity(abs(cdata));
                
                fxSpectra{m}  = fqCData(:,dataColumn);
              
              end
              
              seriesCFqs{m} = fxSpectra{m}; %fqCData(:,dataColumn);
              %seriesCms{m}  = cmage; %retina(image);
              %try delete(fxImages{m}); end
            end
            
            try delete(fxImages{2}); end
            try delete(fxImages{3}); end
            
            CHECK(varTasks); nv = nv -1; %7
                    
            %% Image & Data Consolidation
            
            seriesData = [1:size(seriesFqs{1},1)]';
            for m = 1:numel(sharpImages)
              seriesData  = [seriesData   seriesRFqs{m} ]; % seriesFqs{m}];
            end
            
            for m = 2:3
              seriesImage = [seriesImage  seriesIms{m}  ];
              seriesRmg   = [seriesRmg    seriesRms{m}  ];              
            end
            
            if newRTV || newRES
              scrImage    = [seriesIms{1}; seriesRms{1}];
              hiImage     = [seriesIms{4}; seriesRms{4}];
            end
            
            clear seriesRFqs seriesIms seriesRms;
                        
            for m = 1:numel(fxImages)
              seriesData 	= [seriesData seriesCFqs{m}];
              %seriesCmg  = [seriesCmg seriesCms{m}];
            end
            
            seriesImage       = [seriesImage; seriesRmg];
            
            zv = seriesData([-1:1]+floor(FQ),:);
            [zc zi] = max(zv(:,end)); % yR(ceil(m))]);
            zv = zv(zi,:);
            zv(1) = FQ;
            
            seriesData        = [zv; seriesData];
            runData           = [runData; [RTV CON RES zv]];
            
            imagefile         = [prefix 'image'   suffix '.png'];
            hiFile            = [prefix 'hitone'  hisuffix '.png'];
            scrFile           = [prefix 'screen'  scrsuffix '.png'];
            
            imagefiles{end+1} = imagefile;
            hifiles{end+1}    = hiFile;
            scrfiles{end+1}   = scrFile;
            
            CHECK(varTasks); nv = nv -1; %8
            
            %% Image & Data Output
            
            dlmwrite([prefix 'data' suffix '.txt'], seriesData, '\t');
            imwrite(seriesImage, imagefile); %[prefix 'image' suffix '.png']);
            
            if newRTV || newRES
              imwrite(hiImage,  hiFile);
              imwrite(scrImage, scrFile);
            end

            
            CHECK(varTasks); nv = nv -1; %9
            
            delete(hiContrast); %fxImages
            clear seriesImage seriesData seriesRMG;            
            
          catch err
            disp(err);
          end
          
          if nv>0
            CHECK(varTasks, nv);
          end
        end

        for m = 1:4
          try delete(fxImages{m});    end
          try delete(sharpImages{m}); end
        end
        
        SEAL(varTasks);
        
        CHECK(procTasks); % 2

        %% Series Data Consolidation
        
        lData = runData(:, 9);
        aData = runData(:, 10);
        rData = runData(:, 11);
        zData = runData(:, 12);
        
        lzData = lData ./ zData;
        azData = aData ./ zData;
        rzData = rData ./ zData;
        
        arData = aData ./ rData;
        
        summaryData = [runData lzData azData rzData arData];
        
        CHECK(procTasks); % 3
        
        %% Series Image & Data Output
        
        dlmwrite([prefix outset '-summary.txt'], runData, '\t');
        
        CHECK(procTasks); % 4
        
        %% Series HTML Output
        
        htmlcode{1} = ['<html>' ...
          '<style>' ...
          'body{font-family: Sans-Serif;} ' ...
          'table {border: #ccc 1px solid; border-collapse: collapse; -webkit-text-size-adjust: none; ' ...
          '     font-size: 16px; line-height: 1.5 !important; vertical-align:baseline;} ' ...
          '.blank {background-color: #fff !important; visibility:hidden; width:1% !important;  } ' ...
          'td, th {border: #aaa 1px solid; width: 3%; text-align: center; white-space: nowrap;} ' ...
          'td:nth-of-type(odd)  {background-color: #eee;} ' ...
          '/*th:nth-of-type(odd) {background-color: #aaa;} */' ...
          'th {background-color: #999; font-size:12px;} ' ...
          'img {height: 175 px; width: auto;} ' ...
          'td>b{font-size: 11 px; font-weight: lighter;} ' ...
          'td>span{font-size: 11 px; } ' ... %line-height: 0 px; baseline-shift: baseline;
          '</style>' ...
          '<table>'];
        
        htmlcode{2} = ['<thead><tr>' ...
          '<th>TV</th> <th>CON</th> <th>RES</th> <th>FF</th>  <th class="blank"></th>' ...
          '<th>SCF</th> <th>APF</th> <th>CTF</th> <th>HCF</th>  <th class="blank"></th>' ...
          '<th>SC&#x2a2f;HC</th> <th>AP&#x2a2f;HC</th> <th>CT&#x2a2f;HC</th> <th>HC&#x2a2f;HC</th>' ...
          ' <th class="blank"></th>' ... % '<th>SH/HH</th> <th>PH/HH</th> <th>CH/HH</th> <th>PH/CH</th>' ...
          '<th>IMAGE</th>' ...          
          '</tr></thead>'];
        
        for m = 1:size(runData,1)
          
          tvalue = runData(m,1);
          cvalue = runData(m,2);
          rvalue = runData(m,3);
          
          %imagefile = imagefiles{m}; ...
          [pth fname ext] = fileparts(imagefiles{m});
          imagefile = [fname ext];
          
          [pth fname ext] = fileparts(hifiles{m});
          hifile = [fname ext];
          
          [pth fname ext] = fileparts(scrfiles{m});
          scrfile = [fname ext];          
                   
          imgcode   = sprintf('<td><img src="%s" /><img src="%s" /><img src="%s" /></td>', scrfile, imagefile, hifile);
          
          datacode  = sprintf([ ...
            '<td>TV<br/>%0.0fe+0</td>  <td>CON<br/>%2.1fe+0</td>  <td>RES<br/>%4.3fe+0</td>  <td>FF<br/>%2.1fe+0</td> <td class="blank"></td>' ...
            '<td>SCF<br/>%3.2fe%+d</td>  <td>APF<br/>%3.2fe%+d</td>  <td>CTF<br/>%3.2fe%+d</td>  <td>HCF<br/>%3.2fe%+d</td> <td class="blank"></td>' ...
            '<td>SC&#x2a2f;HC<br/>%3.2fe%+d</td> <td>AP&#x2a2f;HC<br/>%3.2fe%+d</td> <td>CT&#x2a2f;HC<br/>%3.2fe%+d</td> <td>HC&#x2a2f;HC<br/>%3.2fe%+d</td> ' ...
            ... % '<td>%3.2fe%+d</td><td>%3.2fe%+d</td><td>%3.2fe%+d</td><td>%3.2fe%+d</td>' ...
            ], ...
            runData(m,1:4), sciparts(runData(m, 5:8)), sciparts(runData(m, 9:12)) ... %runData(m,1:4), runData(m,9:12), lData, aData, rData, zData, ...
            ... %lzData(m), azData(m), rzData(m), arData(m) ...
            );
          
          
          datacode  = regexprep(datacode, '(<td>)([^\<]*)(<br/>)', '$1<b>$2</b>$3');
          datacode  = regexprep(datacode, '(\d\.\d+)(e)([-+])(0?)([1-9]+)', '$1<br/><span>e$3$5</span>');
          datacode  = regexprep(datacode, '([\d\.]+)(e)([-+])(0)', '$1<br/><span>&nbsp;</span>');
          
          rowcode   = ['<tr>' datacode '<td class="blank"></td>' imgcode '</tr>'];
          
          htmlcode{end+1} = rowcode;
        end
        
        htmlcode{end+1} = '</table></html>';
        
        CHECK(procTasks); % 5
        
        dlmwrite([prefix outset '-summary.html'], strvcat(htmlcode),'');
        
        CHECK(procTasks); % 6
        
      catch err
        disp(err);
      end
      
      %% Series Finalization
      
      % try uiopen([prefix 'data-summary.html'],1); end
              
      parameters.Patch.Mean         = tsetting;
      parameters.Patch.Contrast     = csetting;
      parameters.Patch.Resolution   = rsetting;
        
      drawnow expose update;
      
      SEAL(procTasks);
      
    end
    
    
    function output = Run(obj)
      
      %debugging = true;
      
      delete(timerfindall);
      
      %% Progress Initialization
      obj.UpdateProgressComponents;
      
      obj.ProcessProgress.Window = obj.View.Window;
            
      obj.ProcessProgress.resetTasks;
      
      
      %% Fixed Tasks Load Estimation
      prepTasks = obj.ProcessProgress.addTask('Prepare Process Components', 4);
      procTasks = obj.ProcessProgress.addTask('Executing Processes', 4);
      
      obj.ProcessProgress.activateTask(prepTasks);
      
      %% Variable Tasks Load Estimation      
      n         = numel(fieldnames(obj.Parameters.Processors));
      n         = n + 2*(n-5) + 2;  % Patch Rendering
      varTasks  = obj.ProcessProgress.addTask('Executing Processes', n);
      
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
        
        obj.ProcessProgress.activateTask(varTasks);
        
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
          
          obj.ProcessProgress.activateTask([]);
          
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
  
  methods (Static)
    function output = GenerateHTML(runData, filename, prefix)
      
      if ~exist('prefix', 'var')
        prefix = '';
      end
      
      if ~exist('filename', 'var')
        filename = '';
      end
      if exist(runData, 'file')>0
        if isempty(prefix) && ~isempty(filename) %~exist('prefix', 'var') && nargin==2
          prefix = filename;
        end
        filename = runData;
        runData = dlmread(filename, '\t');
      else
        if isempty(filename)
          filename = fullfile('Output', 'series-summary.txt');
        end
      end
      
      [pth fname ext] = fileparts(filename);
      
      if isempty(prefix)
        prefix = regexprep(fname, '[-]?summary', '');
      end
      
      outset = regexprep(regexprep(fname, '[-]?summary', ''), 'series-\d+-', '');
      
      
        htmlcode{1} = ['<html>' ...
          '<style>' ...
          'body{font-family: Sans-Serif;} ' ...
          'table {border: #ccc 1px solid; border-collapse: collapse; -webkit-text-size-adjust: none; ' ...
          '     font-size: 16px; line-height: 1.5 !important; vertical-align:baseline;} ' ...
          '.blank {background-color: #fff !important; visibility:hidden; width:1% !important;  } ' ...
          'td, th {border: #aaa 1px solid; width: 3%; text-align: center; white-space: nowrap;} ' ...
          'td:nth-of-type(odd)  {background-color: #eee;} ' ...
          '/*th:nth-of-type(odd) {background-color: #aaa;} */' ...
          'th {background-color: #999; font-size:12px;} ' ...
          'img {height: 175 px; width: auto;} ' ...
          'td>b{font-size: 11 px; font-weight: lighter;} ' ...
          'td>span{font-size: 11 px; } ' ... %line-height: 0 px; baseline-shift: baseline;
          '</style>' ...
          '<table>'];
        
        htmlcode{2} = ['<thead><tr>' ...
          '<th>TV</th> <th>CON</th> <th>RES</th> <th>FF</th>  <th class="blank"></th>' ...
          '<th>SCF</th> <th>APF</th> <th>CTF</th> <th>HCF</th>  <th class="blank"></th>' ...
          '<th>SC&#x2a2f;HC</th> <th>AP&#x2a2f;HC</th> <th>CT&#x2a2f;HC</th> <th>HC&#x2a2f;HC</th>' ...
          ' <th class="blank"></th>' ... % '<th>SH/HH</th> <th>PH/HH</th> <th>CH/HH</th> <th>PH/CH</th>' ...
          '<th>IMAGE</th>' ...          
          '</tr></thead>'];
        
        for m = 1:size(runData,1)
          
          RTV = runData(m,1);
          CON = runData(m,2);
          RES = runData(m,3);
          FQ  = runData(m,4);
          
            suffix        = [ ...
              '-V' num2str(round(RTV),    '%0.3d') ...
              '-R' num2str(round(RES*100),'%0.3d') ...
              '-C' num2str(round(CON*10), '%0.4d') ...
              '-F' num2str(round(FQ*10),  '%0.3d')];
            
            hisuffix        = [ ...
              '-V' num2str(round(RTV),    '%0.3d') ...
              '-R' num2str(round(RES*100),'%0.3d') ...
              '-F' num2str(round(FQ*10),  '%0.3d')];
            
            scrsuffix        = [ ...
              '-V' num2str(round(RTV),    '%0.3d')]; % ...
              %'-R' num2str(round(RES*100),'%0.3d') ...
              %'-F' num2str(round(FQ*10),  '%0.3d')];
          
            imagefile         = [prefix '-image'   suffix '.png'];
            hifile            = [prefix '-hitone'  hisuffix '.png'];
            scrfile           = [prefix '-screen'  scrsuffix '.png'];
          
%           %imagefile = imagefiles{m}; ...
%           [pth fname ext] = fileparts(imagefiles{m});
%           imagefile = [fname ext];
%           
%           [pth fname ext] = fileparts(hifiles{m});
%           hifile = [fname ext];
%           
%           [pth fname ext] = fileparts(scrfiles{m});
%           scrfile = [fname ext];          
                   
          imgcode   = sprintf('<td><img src="%s" /><img src="%s" /><img src="%s" /></td>', scrfile, imagefile, hifile);
          
          datacode  = sprintf([ ...
            '<td>TV<br/>%0.0fe+0</td>  <td>CON<br/>%2.1fe+0</td>  <td>RES<br/>%4.3fe+0</td>  <td>FF<br/>%2.1fe+0</td> <td class="blank"></td>' ...
            '<td>SCF<br/>%3.2fe%+d</td>  <td>APF<br/>%3.2fe%+d</td>  <td>CTF<br/>%3.2fe%+d</td>  <td>HCF<br/>%3.2fe%+d</td> <td class="blank"></td>' ...
            '<td>SC&#x2a2f;HC<br/>%3.2fe%+d</td> <td>AP&#x2a2f;HC<br/>%3.2fe%+d</td> <td>CT&#x2a2f;HC<br/>%3.2fe%+d</td> <td>HC&#x2a2f;HC<br/>%3.2fe%+d</td> ' ...
            ... % '<td>%3.2fe%+d</td><td>%3.2fe%+d</td><td>%3.2fe%+d</td><td>%3.2fe%+d</td>' ...
            ], ...
            runData(m,1:4), sciparts(runData(m, 5:8)), sciparts(runData(m, 9:12)) ... %runData(m,1:4), runData(m,9:12), lData, aData, rData, zData, ...
            ... %lzData(m), azData(m), rzData(m), arData(m) ...
            );
          
          
          datacode  = regexprep(datacode, '(<td>)([^\<]*)(<br/>)', '$1<b>$2</b>$3');
          datacode  = regexprep(datacode, '(\d\.\d+)(e)([-+])(0?)([1-9]+)', '$1<br/><span>e$3$5</span>');
          datacode  = regexprep(datacode, '([\d\.]+)(e)([-+])(0)', '$1<br/><span>&nbsp;</span>');
          
          rowcode   = ['<tr>' datacode '<td class="blank"></td>' imgcode '</tr>'];
          
          htmlcode{end+1} = rowcode;
        end
        
        htmlcode{end+1} = '</table></html>';
        
        %CHECK(procTasks); % 5
        
        dlmwrite(fullfile(pth, [prefix '-' outset '-summary.html']), strvcat(htmlcode),'');
        
        %CHECK(procTasks); % 6
      
    end
    
  end
  
end

