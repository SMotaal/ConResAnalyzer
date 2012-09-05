classdef PatchSeriesProcessor < Grasppe.ConRes.PatchGenerator.PatchGeneratorProcessor
  %PATCHSERIESPROCESSOR Run patch generator series
  %   Detailed explanation goes here
  
  properties
    Codes
  end
  
  methods
    
    output = Run(obj)
    
    updateTasks(obj, activeTask, taskTitle, numRender, numAnalyze, numExport, numCompile);
    prepareTasks(obj, title, numPrep, numRender, numAnalyze, numExport, numCompile);
    
    
    parameters          = PrepareParameters(obj);
    
    str                 = ParameterString(obj, parameters);
    str                 = ParameterID(obj, parameters, type);
    
    fieldOutput         = GetOutputField(obj, fieldName, overwriteFields, output);
    
    [pth exists folder] = GetPath(obj, type, id, ext)
    
    SaveOutput(obj, output);
    output              = LoadOutput(obj, field);
    
    pth                 = SaveImage(obj, img, type, id);
    img                 = LoadImage(obj, type, id);
    [pths imgs]         = OutputImages(obj, src, type, id, retinalAccuity, imageOut);
    
    fieldRange          = FilterFields(obj, table, varargin);
    
    fields              = ProcessFields(obj, parameters);
    grids               = GenerateGrids(obj, fields);
    
    series              = GenerateSeriesImages(obj, grids, fields, processors, parameters, task);
    output              = Sandbox(obj, varargin);
  end
end


%     function output = Run(obj)
%
%       import(eval(NS.CLASS));
%       %debugging = true;
%
%       obj.ResetProcess();
%       obj.prepareTasks('Patch Generation', 4);
%
%       drawnow expose update;
%
%       updateFields        = {'Series', 'Fields', 'Grids'};
%       overwriteFields     = {'Processors', 'Parameters', updateFields{:}};
%       output              = struct;
%       %obj.LoadOutput;
%
%       try
%         processors  = obj.GetOutputField('Processors', overwriteFields, output);
%         if isempty(processors)
%
%           %% Prepare Processors
%
%           processors.Patch    = obj.PatchProcessor;
%           processors.Screen   = obj.ScreenProcessor;
%           processors.Scan     = obj.ScanProcessor;
%         end
%
%         parameters  = obj.GetOutputField('Parameters', overwriteFields, output);
%         if isempty(parameters)
%           parameters          = obj.PrepareParameters;
%         end
%         %determine parameters
%       catch err
%         debugStamp(err, 1);
%       end
%
%       output.Parameters   = parameters;
%       output.Processors   = processors;
%
%
%       CHECK(obj.Tasks.Prep);
%       % PREP 1/4 ***************************************************************
%
%       try
%         fields          = obj.GetOutputField('Fields');
%         if isempty(fields), fields  = obj.ProcessFields(parameters);  end
%
%         output.Fields   = fields;
%
%         grids           = obj.GetOutputField('Grids');
%         if isempty(grids),  grids   = obj.GenerateGrids(fields);      end
%
%         output.Grids    = grids;
%       catch err
%         debugStamp(err, 1);
%       end
%
%       % obj.SaveOutput;
%
%       CHECK(obj.Tasks.Prep);
%       % PREP 2/4 ***************************************************************
%
%       try
%         %% Prepare Processors
%
%       catch err
%         debugStamp(err, 1);
%       end
%
%       % PREP 3/4 ***************************************************************
%       CHECK(obj.Tasks.Prep);
%       %
%       %       %% Variable Tasks Load Estimation
%       %       nPatches    = [halftoneRows];
%       %       nExtras     = [halftoneRows, screenRows, contoneRows, monotoneRows];
%       %       nImages     = 2;
%       %       nStatistics = 1;
%       %       nSteps      = [nImages nStatistics]; % [Image+Retina Statistics
%       %       n         = numel(fieldnames(obj.Parameters.Processors));
%       %       n         = n + 2*(n-5) + 2;
%       %
%       SEAL(obj.Tasks.Prep); % 3
%
%       % obj.updateTasks('Generating Patch', n);
% %       try
%         %series  = obj.GetOutputField('Series');
%         %if isempty(series), series = obj.GenerateSeriesImages([], [], [], [], obj.Tasks.Render); end
%
%         series = obj.GenerateSeriesImages([], [], [], [], obj.Tasks.Render);
%
%         output.Series = series;
%       %series  = GeneratePatches(grids, fields, processors, parameters, obj.Tasks.Render);
% %       catch err
% %         debugStamp(err, 1);
% %         rethrow(err);
% %       end
%
%       T = tic;
%       seriesStr             = output.Series.Table;
%       seriesStr(:, 3:end)   = strrep(lower(seriesStr(:,3:end)), '-', '  ');
%       output.Series.Report  = mat2clip(seriesStr);
%       toc(T);
%
%       obj.SaveOutput;
%
%       %output = seriesStr; %[]; %{output{:}, strvcat(seriesStr)};
%
%     end

%     function fieldOutput = GetOutputField(obj, fieldName, overwriteFields, output)
%       try if ~exist('overwriteFields', 'var')
%           overwriteFields = evalin('caller', 'overwriteFields'); end;
%       end;
%       try if ~exist('output', 'var')
%           output          = evalin('caller', 'output'); end;
%       catch err
%         output            = struct;
%       end;
%
%       fieldOutput         = [];
%       try
%         if isfield(output, fieldName) || any(strcmpi(fieldName, overwriteFields));
%           fieldOutput     = output.(fieldName);
%         else
%           fieldOutput     = obj.LoadOutput(fieldName);
%         end
%       end
%     end

%     function SaveOutput(obj, output)
%       persistent outputPath
%
%       try if ~exist('output', 'var')
%           output      = evalin('caller', 'output'); end; end;
%
%       if isempty(outputPath)
%         outputPath  = obj.GetPath('output', 'series', 'mat');
%       end
%
%       save(outputPath, '-struct', 'output');
%     end

%     function output = LoadOutput(obj, field)
%       persistent outputPath
%
%       if isempty(outputPath)
%         outputPath  = obj.GetPath('output', 'series', 'mat');
%       end
%
%       output        = [];
%
%       try
%         if exist('field', 'var')
%           output    = load(outputPath, field);
%           if nargout==1 && numel(fieldnames(output))==1
%             output  = output.(field);
%             return;
%           end
%         else
%           field     = [];
%         end
%       end
%
%       if isempty(output)
%         output      = load(outputPath); %, '-regexp', '^(O|o)utput$');
%       end
%
%       %       F = fieldnames(S);
%       %       output = S.(F{1});
%
%       try if nargout==0 && isempty(field)
%           assignin('caller', 'output', output); end; end;
%     end

%     function pth = SaveImage(obj, img, type, id)
%       try
%         %if isstruct(id)
%         if isempty(strfind(lower(type), 'image'))
%           type = [strtrim(type) ' image'];
%         end
%         pth = obj.GetPath(type, id, 'png');
%         imwrite(img, pth);
%       catch err
%         debugStamp(err, 1);
%       end
%     end

%     function img = LoadImage(obj, type, id)
%       if isempty(strfind(lower(type), 'image'))
%         type = [strtrim(type) ' image'];
%       end
%       pth = obj.GetPath(type, id, 'png');
%       try img = imread(pth); end
%     end

%     function [pth exists folder] = GetPath(obj, type, id, ext)
%       seriesFolder = 'Series101';
%
%       if exist('id', 'var')
%         if isstruct(id)
%           id = obj.ParameterID(id, type);
%         end
%       else
%         id = [];
%       end
%
%       if exist('ext', 'var') && ~isempty(id);
%         ext = ['.' regexprep(ext,'\W','')];
%       else
%         ext = [];
%       end
%
%       groupFolder       = regexpi(type, '\<(screen|halftone|contone|monotone)\>', 'match');
%       if numel(groupFolder)==1
%         groupFolder = groupFolder{1};
%         groupFolder(1)  = upper(groupFolder(1));
%       else
%         groupFolder     = []; %'Others';
%       end
%
%       subFolder         = regexpi(type, '\<(image|fftimage|retinaimage|data|output|report)\>', 'match');
%       if numel(subFolder)==1
%         subFolder       = subFolder{1};
%         subFolder(1)    = upper(subFolder(1));
%       else
%         subFolder       = [];
%       end
%
%       switch lower(subFolder)
%         case {'image', 'report', 'retinaimage', 'fftimage'}
%           parentFolder  = [];
%         otherwise
%           parentFolder  = 'Resources';
%       end
%
%       folder            = fullfile('Output', seriesFolder, parentFolder, groupFolder, subFolder);
%
%       pth               = fullfile(folder, [id ext]);
%
%       if ~isempty(folder) && exist(folder, 'dir')~=7
%         FS.mkDir(folder);
%       end
%
%       if nargout>1
%         if ~isempty(id)
%           exists        = exist(pth, 'file')>0;
%         else
%           exists        = exist(pth, 'dir')>0;
%         end
%       end
%     end

%     function [pths imgs] = OutputImages(obj, src, type, id, retinalAccuity, imageOut)
%       pths                    = cell(1, 2);
%       imgs                    = cell(1, 2);
%
%       retinaOut = exist('retinalAccuity', 'var') & isscalar(retinalAccuity) & isnumeric(retinalAccuity);
%       imageOut  = exist('imageOut',  'var') | isequal(imageOut , false);
%
%       if ~imageOut
%         pths{1}               = {obj.GetPath([type ' image'], id, 'png')};
%         if retinaOut, pths{2} = {obj.GetPath([type ' retinaImage'], id, 'png')}; end
%         return;
%       end
%
%       if isnumeric(src)
%         imgs{1}               = src;
%       elseif isobject(src)
%         imgs{1}               = src.Image;
%       end
%
%       pths{1}                 = obj.SaveImage(imgs{1}, type, id);
%
%       if ~retinaOut, return; end
%
%       disk                    = @(x, y)     imfilter(x,fspecial('disk',y),'replicate');
%
%       imgs{2}                 = disk(imgs{1}, retinalAccuity);
%       pths{2}                 = obj.SaveImage(imgs{2}, [type ' retinaImage'], id);
%     end

%     function series = GenerateSeriesImages(obj, grids, fields, processors, parameters, task)
%
%       forceGenerate       = false;
%       imageTypes          = {'halftone', 'screen', 'contone', 'monotone'};
%       halftoneOutput      = true;
%       retinaOutput        = true;
%
%       try
%         OUTPUT            = evalin('caller', 'output');
%         setOuput          = true;
%
%         try if ~exist('grids', 'var')   || isempty(grids)
%             grids         = OUTPUT.Grids; end;
%         end
%
%         try if ~exist('fields', 'var')  || isempty(fields)
%             fields        = OUTPUT.Fields; end;
%         end
%
%         try if ~exist('parameters', 'var')  || isempty(parameters)
%             parameters    = OUTPUT.Parameters; end;
%         end
%
%         try if ~exist('processors', 'var')  || isempty(processors)
%             processors    = OUTPUT.Processors; end;
%         end
%
%       catch err
%         setOuput          = false;
%       end
%
%       setTask             = exist('task', 'var') && isa(task, 'Grasppe.Occam.ProcessTask');
%
%       fieldTable          = fields.Table;
%       seriesRows          = grids.Halftone.Rows;
%       seriesRange         = 1:seriesRows;
%
%       seriesTable         = cell(seriesRows, 6);
%
%       halftoneIDs         = cell(seriesRows, 1);
%       screenIDs           = halftoneIDs;
%       contoneIDs          = halftoneIDs;
%       monotoneIDs         = halftoneIDs;
%
%       halftonePaths       = cell(seriesRows, 2);
%       screenPaths         = halftonePaths;
%       contonePaths        = halftonePaths;
%       monotonePaths       = halftonePaths;
%
%       screenIdxs          = grids.Screen.Index;
%       contoneIdxs         = grids.Contone.Index;
%       monotoneIdxs        = grids.Monotone.Index;
%
%       screenRefs          = grids.Screen.Reference;
%       contoneRefs         = grids.Contone.Reference;
%       monotoneRefs        = grids.Monotone.Reference;
%
%       %% Prepare Parameters Struct
%       fieldNames                    = unique(fieldTable(:,1), 'stable');
%       seriesStruct                  = cell(numel(fieldNames)*2, 1);
%       seriesStruct(1:2:end)         = fieldNames;
%       seriesParameters(seriesRange) = struct(seriesStruct{:});
%
%       parfor m = 1:seriesRows
%
%         patchProcessor            = [];
%         screenProcessor           = [];
%         scanProcessor             = [];
%         output                    = [];
%         referenceImage            = [];
%
%         %% Outputs
%         screenOutput              = any(m==screenIdxs);
%         contoneOutput             = any(m==contoneIdxs);
%         monotoneOutput            = any(m==monotoneIdxs);
%
%         %% References
%         screenRef                 = screenRefs(m);
%         contoneRef                = contoneRefs(m);
%         monotoneRef               = monotoneRefs(m);
%
%         %% Parameters
%         parameters                = struct;
%         valueIndex                = grids.Halftone.Grid(m, :);
%
%         for n = 1:size(fieldTable, 1)
%           v                       = valueIndex(n);
%           if v>0, v               = fieldTable{n,3}(v); end
%           parameters.(fieldTable{n,1}).(fieldTable{n,2}) = v;
%         end
%
%         parameters.Screen.PixelResolution = obj.PatchProcessor.Addressability;
%
%         seriesParameters(m)       = parameters;
%
%         %% Retina Filter Calculation
%         pixelResolution           = parameters.Scan.Resolution*parameters.Scan.Scale/100/25.4;
%         viewingDistance           = 30*10; % mm
%         retinalAccuity            = 1/60 * pi/180;
%         retinalResolution         = viewingDistance*(tan(retinalAccuity/2));
%         pixelAccuity              = pixelResolution*retinalResolution*7;
%
%         %% Detemine IDs
%         halftoneID                = obj.ParameterID(parameters);
%         screenID                  = [];
%         contoneID                 = [];
%         monotoneID                = [];
%         if screenOutput
%           screenID                = obj.ParameterID(parameters, 'screen');    end
%         if contoneOutput
%           contoneID               = obj.ParameterID(parameters, 'contone');   end
%         if monotoneOutput
%           monotoneID              = obj.ParameterID(parameters, 'monotone');  end
%
%
%         %% Load Images
%         generateImages            = true;
%
%         try
%           halftoneImage           = obj.LoadImage('halftone', halftoneID);
%
%           if screenOutput || ~forceGenerate
%             screenImage           = obj.LoadImage('screen', screenID);
%             screenRetina          = obj.LoadImage('screen retinaImage', screenID);
%           end
%
%           if contoneOutput || ~forceGenerate
%             contoneImage          = obj.LoadImage('contone', contoneID);
%             contoneRetina         = obj.LoadImage('contone retinaImage', contoneID);
%           end
%           if monotoneOutput || ~forceGenerate
%             monotoneImage         = obj.LoadImage('monotone', monotoneID);
%             monotoneRetina        = obj.LoadImage('monotone retinaImage', monotoneID);
%           end
%
%           generateImages          = false;
%         catch err
%           debugStamp(err,5);
%         end
%
%         %% Processors
%         if generateImages
%           patchProcessor          = Grasppe.ConRes.PatchGenerator.Processors.Patch;
%           screenProcessor         = Grasppe.ConRes.PatchGenerator.Processors.Screen;
%           scanProcessor           = Grasppe.ConRes.PatchGenerator.Processors.Scan;
%
%           %% Image Models
%           output                  = Grasppe.ConRes.PatchGenerator.Models.ProcessImage;
%
%           if retinaOutput
%             output.Variables.PixelAccuity   = pixelAccuity; % Pixels per mm for theta 1/60
%           end
%
%           %% Generate Patch Image
%           patchProcessor.Execute(parameters.Patch, output);
%           patchImage              = output.Image;
%
%           %% Generate Halftone Image
%           screenProcessor.Execute(parameters.Screen, output);
%           screenImage             = screenProcessor.HalftoneImage.Image;
%
%           scanProcessor.Execute(parameters.Scan, output);
%           scanImage               = output.Image;
%
%           if screenOutput
%             screenImage           = imresize(screenImage,  size(scanImage));
%           end
%
%           if contoneOutput || monotoneOutput
%             referenceImage        = imresize(patchImage,  size(scanImage));
%           end
%
%         else
%           scanImage       = [];
%           screenImage     = [];
%           referenceImage  = [];
%         end
%
%         typeID                    = '';
%         outFlags                  = {pixelAccuity, generateImages};
%
%         %% Output Halftone, Screen, Contone, Monotone (or use reference)
%         if halftoneOutput
%           typeID                  = [typeID 'H'];
%           halftoneIDs{m}          = halftoneID;
%           [pths imgs]             = obj.OutputImages(scanImage, 'halftone', halftoneID, outFlags{:});
%           halftonePaths(m,:)      = pths;
%         end
%         if screenOutput
%           typeID                  = [typeID 'S'];
%           screenIDs{m}            = screenID;
%           [pths imgs]             = obj.OutputImages(screenImage, 'screen', screenID, outFlags{:});
%           screenPaths(m,:)        = pths;
%         end
%         if contoneOutput
%           typeID                  = [typeID 'C'];
%           contoneIDs{m}           = contoneID;
%           [pths imgs]             = obj.OutputImages(referenceImage, 'contone', contoneID, outFlags{:});
%           contonePaths(m,:)       = pths;
%         end
%         if monotoneOutput
%           typeID                  = [typeID 'M'];
%           monotoneIDs{m}          = monotoneID;
%           [pths imgs]             = obj.OutputImages(referenceImage, 'monotone', monotoneID, outFlags{:});
%           monotonePaths(m,:)      = pths;
%         end
%
%         if generateImages
%           try delete(patchProcessor);   end
%           try delete(screenProcessor);  end
%           try delete(scanProcessor);    end
%           try delete(output);           end
%           try delete(screenImage);      end
%         end
%
%         seriesTable(m,:)          = {numel(typeID), typeID, [], halftoneID, [], []};
%       end
%
%       %% References
%       screenIDs                   = screenIDs(screenIdxs);
%       contoneIDs                  = contoneIDs(contoneIdxs);
%       monotoneIDs                 = monotoneIDs(monotoneIdxs);
%
%       screenPaths                 = screenPaths(screenIdxs);
%       contonePaths                = contonePaths(contoneIdxs);
%       monotonePaths               = monotonePaths(monotoneIdxs);
%
%       seriesTable(:, 3)           = screenIDs(screenRefs);
%       seriesTable(:, 4)           = halftoneIDs;
%       seriesTable(:, 5)           = contoneIDs(contoneRefs);
%       seriesTable(:, 6)           = monotoneIDs(monotoneRefs);
%
%       series.Table                = seriesTable;
%       series.Parameters           = seriesParameters;
%       series.Paths.Halftone       = halftonePaths;
%       series.Paths.Screen         = screenPaths;
%       series.Paths.Contone        = contonePaths;
%       series.Paths.Monotone       = monotonePaths;
%
%       if setOuput || nargout==0
%         OUPUT.Series              = series;
%         assignin('caller', 'output', OUPUT);
%       end
%     end

%     function fields   = ProcessFields(obj, parameters)
%       output        = struct;
%       setOuput      = false;
%       try
%         output      = evalin('caller', 'output');
%         setOuput    = true;
%       end
%
%       %% Determine Variable Fields
%
%       fieldTable  = cell(0,3);
%       fieldVars   = [];
%
%       parameterGroups = fieldnames(parameters);
%       try
%         for m = 1:numel(parameterGroups)-1
%           groupName       = parameterGroups{m};
%           groupParameters = parameters.(groupName);
%           try
%             groupFields     = fieldnames(groupParameters);
%             for n = 1:numel(groupFields)
%               try
%                 fieldName                   = groupFields{n};
%                 fieldValue                  = parameters.(groupName).(fieldName);
%                 fieldTable(end+1, 1:3)  = {groupName, fieldName, fieldValue};
%                 if ~ischar(fieldValue) && numel(fieldValue)>1
%                   fieldVars(end+1)       = numel(fieldValue);
%                 else
%                   fieldVars(end+1)       = 1;
%                 end
%               catch err
%                 debugStamp(err, 2);
%               end
%             end
%           catch err
%             debugStamp(err, 2);
%           end
%         end
%
%         fieldCount  = numel(fieldVars);
%       catch err
%         debugStamp(err, 1);
%       end
%
%       % fieldRange        = cell(fieldCount,1);
%
%       %% Geenrate Field Filters
%
%       selectFields            = @(varargin) obj.FilterFields(fieldTable,varargin{:});
%       filterFields            = @(fields)   setdiff(fields, 1:size(fieldCount));
%
%       screenFields            = selectFields('Mean', 'Size', 'Screen', 'Scan');
%       contoneFields           = selectFields('Patch', 'Addressability', 'Scan');
%       monotoneFields          = selectFields('Resolution', 'Size', 'Addressability', 'Scan');
%
%       screenFilter            = filterFields(screenFields);
%       contoneFilter           = filterFields(contoneFields);
%       monotoneFilter          = filterFields(monotoneFields);
%
%       %% Compose Fields Structure
%
%       fields.Count            = fieldCount;
%       % fields.Range            = fieldRange;
%       fields.Variables        = fieldVars;
%       fields.Table            = fieldTable;
%       fields.Groups           = parameterGroups;
%
%       fields.Fields.Screen    = screenFields;
%       fields.Fields.Contone   = contoneFields;
%       fields.Fields.Monotone  = monotoneFields;
%
%       fields.Filters.Screen   = screenFilter;
%       fields.Filters.Contone  = contoneFilter;
%       fields.Filters.Monotone = monotoneFilter;
%
%       output.Fields           = fields;
%
%       if setOuput, assignin('caller', 'output', output); end
%     end

%     function grids = GenerateGrids(obj, fields)
%       output        = struct;
%       setOuput      = false;
%       try
%         output      = evalin('caller', 'output');
%         setOuput    = true;
%       end
%
%       %% Generate Variable Grid
%
%       fieldCount    = fields.Count;
%       % fieldRange  = fields.Range;
%       fieldVars     = fields.Variables;
%       fieldTable    = fields.Table;
%       fieldFilters  = fields.Filters;
%
%
%       varRange    = '';
%       for m = 1:fieldCount
%         fieldRange{m} = 1:fieldVars(m);
%         varRange  = [varRange 'varGrid{' int2str(m) '} '];
%       end
%
%       eval(['[' varRange '] = ndgrid(fieldRange{:});']);
%
%       varCount      = numel(varGrid{1});
%
%       %% Compile Patch-Variable Index Grid
%
%       patchGrid   = zeros(fieldCount, varCount);
%
%       for m = 1:fieldCount
%         patchGrid(m,:) = varGrid{m}(:);
%       end
%       patchGrid = patchGrid';
%
%       try
%
%         % Determine Valid Halftone Patch Grid
%         meanRange                   = fieldTable{1,3}(patchGrid(:,1));
%         conRange                    = fieldTable{2,3}(patchGrid(:,2));
%         resRange                    = fieldTable{3,3}(patchGrid(:,3));
%         halftoneGrid                = patchGrid((meanRange+conRange./2)<=100 & (meanRange-conRange./2)>=0, :);
%         halftoneRows                = size(halftoneGrid,1);
%
%         % Determine Screening Patch Index
%         screenFilter                = fields.Fields.Screen;
%         [g screenIdx screenRef]     = unique(halftoneGrid(:, screenFilter), 'rows', 'stable');
%         screenGrid                  = halftoneGrid(screenIdx, :);
%         screenRows                  = size(screenGrid,1);
%
%         % Determine Contone Patch Index
%         contoneFilter               = fields.Fields.Contone;
%         [g contoneIdx contoneRef]   = unique(halftoneGrid(:, contoneFilter), 'rows', 'stable');
%         contoneGrid                 = halftoneGrid(contoneIdx, :);
%         contoneRows                 = size(contoneGrid,1);
%
%         % Determine Monotone Patch Index
%         monotoneFilter              = fields.Fields.Monotone;
%         [g monotoneIdx monotoneRef] = unique(halftoneGrid(:, monotoneFilter), 'rows', 'stable');
%         monotoneGrid                = halftoneGrid(monotoneIdx, :);
%         monotoneRows                = size(monotoneGrid,1);
%
%
%         % Validate Series Indices
%         validateIdx                 = @(r, g) all(r<=halftoneRows) & all(all(halftoneGrid(r, :)-g==0));
%         screenValid                 = validateIdx(screenIdx,    screenGrid  );
%         contoneValid                = validateIdx(contoneIdx,   contoneGrid );
%         monotoneValid               = validateIdx(monotoneIdx,  monotoneGrid);
%         seriesValid                 = screenValid & contoneValid & monotoneValid;
%
%       catch err
%         debugStamp(err, 1);
%       end
%
%
%       grids.Range.Fields        = fieldRange;
%       grids.Range.Grid          = patchGrid;
%       grids.Range.Rows          = varCount;
%       grids.Range.Columns       = fieldCount;
%       grids.Range.MeanTone      = meanRange;
%       grids.Range.Contrast      = conRange;
%       grids.Range.Resolution    = resRange;
%
%       grids.Halftone.Grid       = halftoneGrid;
%       grids.Halftone.Rows       = halftoneRows;
%       grids.Halftone.Columns    = fieldCount;
%
%       grids.Screen.Grid         = screenGrid;
%       grids.Screen.Index        = screenIdx;
%       grids.Screen.Reference    = screenRef;
%       grids.Screen.Rows         = screenRows;
%       grids.Screen.Columns      = fieldCount;
%
%       grids.Contone.Grid        = contoneGrid;
%       grids.Contone.Index       = contoneIdx;
%       grids.Contone.Reference   = contoneRef;
%       grids.Contone.Rows        = contoneRows;
%       grids.Contone.Columns     = fieldCount;
%
%       grids.Monotone.Grid       = monotoneGrid;
%       grids.Monotone.Index      = monotoneIdx;
%       grids.Monotone.Reference  = monotoneRef;
%       grids.Monotone.Rows       = monotoneRows;
%       grids.Monotone.Columns    = fieldCount;
%
%       output.Grids              = grids;
%       if setOuput, assignin('caller', 'output', output); end
%     end

%     function fieldRange = FilterFields(obj, table, varargin)
%
%       if isstruct(table) && isfield(table, 'Table')
%         table = fields.Table;
%       end
%
%       if ~iscellstr(varargin), error('Grasppe:FilterFields:InvalidNames', ...
%           'Field filters must be specified as cellstr.'); end
%
%       fieldRange = [];
%
%       for m = 1:numel(varargin)
%         try
%           fieldRows     = [];
%
%           fieldNames    = regexp(varargin{m}, '\w+', 'match');
%
%           if numel(fieldNames)== 2
%
%             groupRows   = strcmpi(fieldNames{1}, table(:,1));
%             fieldRows   = strcmpi(fieldNames{2}, table(:,2));
%
%             fieldRows   = find(groupRows+fieldRows==2);
%
%           elseif numel(fieldNames)==1
%
%             fieldRows   = find(strcmpi(fieldNames{1}, table(:,1)));
%
%             if isempty(fieldRows),
%               fieldRows = find(strcmpi(fieldNames{1}, table(:,2)), 1, 'first');
%             end
%
%           end
%
%           fieldRange  = [fieldRange(:); fieldRows(:)]';
%
%         catch err
%           debugStamp(err, 1);
%           continue;
%         end
%
%       end
%
%     end
%
%     function outputs = generatePatch(obj, parameters, outputs )
%
%       screenOuput   = any(strcmpi(outputs, 'screen'));
%       contoneOuput  = any(strcmpi(outputs, 'contone'));
%       monotoneOuput = any(strcmpi(outputs, 'monotone'));
%
%       outputs = struct;
%
%       % output            = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
%       % reference         = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
%       % patch             = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
%       % screen            = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
%
%
%       try
%         %% Generate Patch
%         patchProcessor.Execute(parameters.Patch);
%         patchImage = output.Image;
%
%         CHECK(obj.Tasks.Variable); % 1
%
%         %% Screen & Print Patch
%         parameters.Screen.(Screen.PPI)  = patchProcessor.Addressability;
%
%         screenProcessor.Execute(parameters.Screen);
%
%         screenedImage = output.Image;
%
%         if screenOuput
%           screen          = screenProcessor.HalftoneImage;
%           screenImage     = screen.Image;
%           outputs.Screen  = screen;
%         end
%
%         CHECK(obj.Tasks.Variable); % 2
%
%         %% Scan Patch
%         scanProcessor.Execute(parameters.Scan);
%         scannedImage = output.Image;
%         patch.setImage(scannedImage, output.Resolution);
%
%         CHECK(obj.Tasks.Variable); % 3
%
%         %% Patch & Reference Images
%
%         referenceImage  = imresize(patchImage,  size(scannedImage));
%         reference.setImage(referenceImage, output.Resolution);
%
%         CHECK(obj.Tasks.Variable); % 4
%
%         screenImage     = imresize(screenImage, size(scannedImage));
%         screen.setImage(screenImage, output.Resolution);
%
%         CHECK(obj.Tasks.Variable); % 5
%
%         output.Variables.PatchImage     = patch;
%         output.Variables.ReferenceImage = reference;
%         output.Variables.HalftoneImage  = screen;
%
%         %parameters.Scan.Resolution
%
%         output.Variables.ScanFactor     = parameters.Scan.Resolution*parameters.Scan.Scale/100;
%         output.Variables.ScanDPMM       = output.Variables.ScanFactor/25.4;
%
%         DPMM                            = output.Variables.ScanDPMM;
%
%         VD                              = 30*10; % mm
%         VA                              = 1/60 * pi/180;
%         VR                              = VD*(tan(VA/2));
%
%         retinalAccuity                              = DPMM*VR*7;
%
%         output.Variables.ViewerDistance = VD;
%         output.Variables.EyeAcuity      = VA;
%         output.Variables.EyeResolution  = VR;
%         %=D8*10*(TAN(RADIANS(1/E8/2)))
%         output.Variables.HumanFactor    = PR; %output.Variables.ScanFactor %(output.Variables.ScanFactor/25.4) *0.3;
%
%         output = copy(output);
%         output.Snap('GeneratedPatch');
%
%       catch err
%         try debugStamp(err, 1, obj); catch, debugStamp(); end;
%       end
%     end

%     function str = ParameterID(obj, parameters, type)
%
%       import Grasppe.ConRes.PatchGenerator.Processors.*;
%
%       %% Patch Abbreviations
%
%       if ~exist('type', 'var'), type = 'normal'; end
%
%       switch lower(type)
%         case {'screen', 'scr', 's'}
%           codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
%           codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
%           % codes.Screen.(Screen.PPI)       = {'PPI', 4,  4,  1,    []  };
%           codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
%           codes.Screen.(Screen.LPI)       = {'LPI', 3,  3,  1,    []  };
%           codes.Screen.(Screen.ANGLE)     = {'DEG', 3,  3,  10,   []  };
%           codes.Screen.(Screen.TVI)       = {'TVI', 2,  2,  1,    0   };
%           codes.Screen.(Screen.NOISE)     = {'NPV', 2,  2,  10,   0   };
%           codes.Screen.(Screen.RADIUS)    = {'BPX', 2,  2,  1,    0   };
%           codes.Screen.(Screen.BLUR)      = {'BPV', 3,  3,  1,    0   };
%           codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
%           codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
%         case {'contone', 'cont', 'con', 'c'}
%           codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
%           codes.Patch.(Patch.CONTRAST)    = {'CON', 3,  3,  10,   []  };
%           codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
%           codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
%           codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
%           codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
%           codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
%         case {'monotone', 'mono', 'm'}
%           codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
%           codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
%           codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
%           codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
%           codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
%         otherwise
%           codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
%           codes.Patch.(Patch.CONTRAST)    = {'CON', 3,  3,  10,   []  };
%           codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
%           codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
%
%           %% Screen codes
%           % codes.Screen.(Screen.PPI)       = {'PPI', 4,  4,  1,    []  };
%           codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
%           codes.Screen.(Screen.LPI)       = {'LPI', 3,  3,  1,    []  };
%           codes.Screen.(Screen.ANGLE)     = {'DEG', 3,  3,  10,   []  };
%
%           %% Print codes
%           codes.Screen.(Screen.TVI)       = {'TVI', 2,  2,  1,    0   };
%           codes.Screen.(Screen.NOISE)     = {'NPV', 2,  2,  10,   0   };
%           codes.Screen.(Screen.RADIUS)    = {'BPX', 2,  2,  1,    0   };
%           codes.Screen.(Screen.BLUR)      = {'BPV', 3,  3,  1,    0   };
%
%           %% Scan codes
%           codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
%           codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
%       end
%
%       obj.Codes                       = codes;
%
%
%       parameterGroups = fieldnames(parameters);
%
%       str = '';
%       for m = 1:numel(parameterGroups)
%         groupName           = parameterGroups{m};
%         groupParameters     = parameters.(groupName);
%         groupString         = [groupName '{'];
%         try
%           groupFields       = fieldnames(groupParameters);
%           for n = 1:numel(groupFields)
%             try
%               fieldName     = groupFields{n};
%               fieldValue    = parameters.(groupName).(fieldName);
%
%               fieldCode     = codes.(groupName).(fieldName);
%               fieldLabel    = fieldCode{1};
%               fieldSize     = [fieldCode{2:3}];
%               fieldFactor   = fieldCode{4};
%               fieldDefault  = fieldCode{5};
%
%               if isequal(fieldDefault, fieldValue), continue; end
%
%               if isnumeric(fieldValue)
%                 if isscalar(fieldFactor), fieldValue  = fieldValue.*fieldFactor; end
%                 fieldString     = sprintf('%.0f', fieldValue);
%                 % fieldString   = int2str(fieldValue); %strrep(num2str(fieldValue, ['%0' int2str(fieldSize(1)) '.0f' ]), '.', '');  %'%03.2f'),
%
%                 while numel(fieldString)<fieldSize(1)
%                   fieldString = ['0' fieldString];
%                 end
%
%               elseif ~ischar(fieldValue)
%                 fieldString   = toString(fieldValue);
%               end
%
%               valueLength     = numel(fieldString);
%
%               if isnumeric(fieldSize)
%                 fieldString     = fieldString(1:min(fieldSize(2), max(fieldSize(1), valueLength)));
%               end
%
%               str             = [str '-' fieldLabel fieldString]; %'-'
%               % end
%             end
%           end
%         end
%       end
%
%       str = str(2:end);
%
%     end

%     function str = ParameterString(obj, parameters)
%
%       parameterGroups = fieldnames(parameters);
%
%       str = cell(numel(parameterGroups),1);
%       for m = 1:numel(parameterGroups)
%         groupName         = parameterGroups{m};
%         groupParameters   = parameters.(groupName);
%         groupString       = [groupName '{'];
%         try
%           groupFields     = fieldnames(groupParameters);
%           for n = 1:numel(groupFields)
%             try
%               fieldName   = groupFields{n};
%               fieldValue  = parameters.(groupName).(fieldName);
%               groupString = [groupString fieldName ': ' toString(fieldValue) ';\t'];
%             end
%           end
%         end
%         groupString       = sprintf([strtrim(groupString) '}\t']);
%         str{m,1}          = groupString;
%       end
%
%       str = strcat(str{:}, '\t');
%     end


%     function PrepareParameters(obj)
%       import(eval(NS.CLASS));
%
%       import Grasppe.ConRes.PatchGenerator.Processors.*;
%
%       parameters = Grasppe.ConRes.PatchGenerator.Models.PatchGeneratorParameters;
%
%       parameters.Patch                    = struct();
%       parameters.Screen                   = struct();
%       parameters.Scan                     = struct();
%
%       %% Patch Parameters
%       parameters.Patch.(Patch.MEANTONE)   = 5:15:95;        %[25 50 75]; Grasppe.Kit.ConRes.ToneRange; %[25 50 75];
%       parameters.Patch.(Patch.CONTRAST)   = [33 66 50];     % Grasppe.Kit.ConRes.ContrastRange; % [33 66 50];
%       parameters.Patch.(Patch.RESOLUTION) = [2.155 4.386];  % Grasppe.Kit.ConRes.ResolutionRange; %4.386;
%       parameters.Patch.(Patch.SIZE)       = 5.3;
%
%       %% Screen Parameters
%       parameters.Screen.(Screen.PPI)      = obj.PatchProcessor.Addressability;
%       parameters.Screen.(Screen.SPI)      = 2450;
%       parameters.Screen.(Screen.LPI)      = [100 175];
%       parameters.Screen.(Screen.ANGLE)    = 37.5; %0:7.5:37.5;
%
%       %% Print Parameters
%       parameters.Screen.(Screen.TVI)      = 0;
%       parameters.Screen.(Screen.NOISE)    = 0;
%       parameters.Screen.(Screen.RADIUS)   = 0;
%       parameters.Screen.(Screen.BLUR)     = 0;
%
%       %% Scan Parameters
%       parameters.Scan.(Scan.DPI)          = 1270;
%       parameters.Scan.(Scan.SCALE)        = 100;
%
%       obj.Parameters                      = parameters;
%
%     end


%     function prepareTasks(obj, title, ...
%         numPrep, numRender, numAnalyze, numExport, numCompile)
%       if ~exist('title',      'var'), title       = 'Processing'; end
%       if ~exist('numPrep',    'var'), numPrep     = 3; end
%       if ~exist('numRender' , 'var'), numRender   = 1; end
%       if ~exist('numAnalyze', 'var'), numAnalyze  = 1; end
%       if ~exist('numExport' , 'var'), numExport   = 1; end
%       if ~exist('numCompile', 'var'), numCompile  = 1; end
%
%       obj.Tasks.Prep    = obj.ProcessProgress.addAllocatedTask(['Preparing '  title],   5, numPrep);
%       obj.Tasks.Render  = obj.ProcessProgress.addAllocatedTask(['Rendering '  title],  40, numRender);
%       obj.Tasks.Analyze = obj.ProcessProgress.addAllocatedTask(['Analyzing '  title],  30, numAnalyze);
%       obj.Tasks.Export  = obj.ProcessProgress.addAllocatedTask(['Exporting '  title],  20, numExport);
%       obj.Tasks.Compile = obj.ProcessProgress.addAllocatedTask(['Compiling '  title],   5, numCompile);
%
%
%       obj.ProcessProgress.activateTask(obj.Tasks.Prep);
%
%       drawnow expose update;
%     end

%     function updateTasks(obj, activeTask, taskTitle, numRender, numAnalyze, numExport, numCompile)
%       tasks   = fieldnames(obj.Tasks);  %{'Render', 'Analyze', 'Export', 'Compile'};
%
%       if exist('activeTask') && ~isempty(activeTask) ischar(activeTask)
%         try taskIdx = strcmpi(activeTask, tasks); end
%         if any(taskIdx)
%           try obj.ProcessProgress.activateTask(obj.Tasks.(tasks{taskIdx})); end
%
%           if ~obj.Tasks.Prep.isCompleted(),
%             SEAL(obj.Tasks.Prep);
%           end
%         end
%       end
%
%       if exist('taskTitle', 'var') && ~isempty(taskTitle) && ischar(taskTitle)
%         try obj.ProcessProgress.ActiveTask.Title = taskTitle; end
%       end
%
%       for m = 1:numel(tasks)
%         task = tasks{m};
%
%         numTask = [];               % if exist(['num' task], 'var')
%         try numTask = eval(['num' task]); end
%
%         if isscalar(numTask) && isnumeric(numTask)
%           obj.Tasks.(task).Load  = numTask;
%         end
%       end
%     end
%
%   end

