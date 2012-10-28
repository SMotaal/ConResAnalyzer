function [ output_args ] = exportSeries(data) % SRF, Series )
  %TALLYSRFDATA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.Math;
  
  global forceRenderComposite forceOutputTable;
  
  forceRenderComposite  = false;
  forceOutputTable      = true;
  
  INT                       = '%d';
  DEC                       = @(x) ['%1.' int2str(x) 'f'];
  SCI                       = '"%1.3f E+ %d"';
  STR                       = '%s';
  TAB                       = '\t';
  
  if ~exist('data', 'var')
    data                    = PatchSeriesProcessor.LoadData();
    data.SRF                = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
    data.PRF                = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
  end
  
  parameters                = data.Series.Parameters;
  
  fieldTable                = data.Fields.Table;
  seriesRows                = data.Grids.Halftone.Rows;
  seriesRange               = 1:seriesRows;
  
  htRows                    = data.Grids.Halftone.Rows;
  scRows                    = data.Grids.Screen.Rows;
  ctRows                    = data.Grids.Contone.Rows;
  mtRows                    = data.Grids.Monotone.Rows;
  
  scIdxs                    = data.Grids.Screen.Index;
  ctIdxs                    = data.Grids.Contone.Index;
  mtIdxs                    = data.Grids.Monotone.Index;
  
  scRefs                    = data.Grids.Screen.Reference;
  ctRefs                    = data.Grids.Contone.Reference;
  mtRefs                    = data.Grids.Monotone.Reference;
  
  seriesTable               = data.Series.Table;
  seriesParameters          = data.Series.Parameters;
  seriesVariables           = data.Series.Variables;
  
  htPaths                   = data.Series.Paths.Halftone;
  scPaths                   = data.Series.Paths.Screen;
  ctPaths                   = data.Series.Paths.Contone;
  mtPaths                   = data.Series.Paths.Monotone;
  
  htSRFs                    = data.SRF.Halftone;
  scSRFs                    = data.SRF.Screen;
  ctSRFs                    = data.SRF.Contone;
  mtSRFs                    = data.SRF.Monotone;
  
  htPRFs                    = data.PRF.Monotone.Halftone;
  scPRFs                    = data.PRF.Monotone.Screen;
  ctPRFs                    = data.PRF.Monotone.Contone;
  mtPRFs                    = data.PRF.Monotone.Monotone;
    
  bandWidth   = 1;    % Data Columns = [B isum S imean istd mstd];
  bandSum     = 2;
  filterSum   = 3;
  bandMean    = 4;
  bandSigma   = 5;
  
  %% figure Columns
  plotColumn                = bandMean;
  labelColumns              = [bandMean, bandSigma];
  
  sumTable                  = zeros(seriesRows, 4 + 4 + 4 + 4); % + 4
  outputText                = cell(seriesRows,1);
  
  %% Determine loop and display steps
  mStep                     = 1;  
  mRun                      = 0;  
  dStep                     = 50;
  dNext                     = dStep;
  
  for m = seriesRange(1:mStep:end)
    
    mRun   = mRun + mStep;
    
    try
    
    if mRun>=dNext
      dispf('Generating Series Plots... %d of %d', m, seriesRows);
      dNext = dNext + dStep;
    end
    
    %% Output Checks (All True!)
    
    %% Row Indices
    htIdx                   = m;
    scIdx                   = scRefs(m);
    ctIdx                   = ctRefs(m);
    mtIdx                   = mtRefs(m);
    
    %% Resource IDs
    htID                    = seriesTable{m, 4};
    scID                    = seriesTable{m, 3};
    ctID                    = seriesTable{m, 5};
    mtID                    = seriesTable{m, 6};
    
    %% Spatial Radial Frequency Data
    htSRF                   = htSRFs{htIdx, 2};
    scSRF                   = scSRFs{scIdx, 2};
    ctSRF                   = ctSRFs{ctIdx, 2};
    mtSRF                   = mtSRFs{mtIdx, 2};
    
    %% Cross-Product Spatial Radial Frequency Data
    htPRF                   = htPRFs{htIdx, 2};
    scPRF                   = scPRFs{scIdx, 2};
    ctPRF                   = ctPRFs{ctIdx, 2};
    mtPRF                   = mtPRFs{mtIdx, 2};
    
    %% Patch Parameters
    mRTV                    = parameters(m).Patch.Mean;
    mCON                    = parameters(m).Patch.Contrast;
    mRES                    = parameters(m).Patch.Resolution;
    
    mSize                   = parameters(m).Patch.Size;
    mDPI                    = parameters(m).Scan.Resolution;
    mScale                  = parameters(m).Scan.Scale/100;
    mPPI                    = mDPI*mScale;
    
    %% Patch Variables
    mPixels                 = mDPI*mScale/25.4; %
    mfR                     = Math.VisualResolution(mPPI) * 7;
    mfQ                     = mRES/mSize * mPixels; %mRES/mPixels * mSize/mPixels * 2;
    [mBP mBW]               = Math.FrequencyRange(mSize, mPPI);
    
    %% Fundamental Data Row
    fQRows                  = min(max(1, round(mfQ) + [1:10]), size(mtSRF, 1));
    [fQMax fQRow]           = max(mtSRF(fQRows, bandMean));
    fQRow                   = round(mfQ) + fQRow;
    
    [mCMPPath mCMPExists]   = PatchSeriesProcessor.GetResourcePath('Patch CompositeImage', htID, 'png');
    [mSRFPath mSRFExists]   = PatchSeriesProcessor.GetResourcePath('Patch SRFPlot', htID, 'png');
    [mPRFPath mPRFExists] 	= PatchSeriesProcessor.GetResourcePath('Patch PRFPlot', htID, 'png');
    
    if forceRenderComposite || ~(mCMPExists && mSRFExists && mPRFExists)
      %% Composite Images
      [mComp mW mH]         = composePatchImage(htID, scID, ctID, mtID);
            
      %% Composite Plots
      % columns: [band fftSum fltSum bandMean bandStd binaryStd]
      [mSRFPlots]           = composePatchPlots(mSRFPath, mfQ, fQRow, plotColumn, labelColumns, mW, mH, scSRF, htSRF, ctSRF, mtSRF);
      [mPRFPlots]           = composePatchPlots(mPRFPath, mfQ, fQRow, plotColumn, labelColumns, mW, mH, scPRF, htPRF, ctPRF, mtPRF);
      
      %% Save Images
      % PatchSeriesProcessor.SaveImage(mComp, 'Patch CompositeImage', htID);
      PatchSeriesProcessor.SaveImage(mSRFPlots, 'Patch SRFPlot', htID);
      PatchSeriesProcessor.SaveImage(mPRFPlots, 'Patch PRFPlot', htID);
      
    end
    
    l1 = labelColumns(1);
    l2 = labelColumns(2);
    
    lr = fQRow - 1;
    
    % sumTable(m, :)          = [ ... % mRTV mCON mRES mfQ ...
    %   scSRF(lr, l1) htSRF(lr, l1) ctSRF(lr, l1) mtSRF(lr, l1) ...
    %   scSRF(lr, l2) htSRF(lr, l2) ctSRF(lr, l2) mtSRF(lr, l2) ...
    %   scPRF(lr, l1) htPRF(lr, l1) ctPRF(lr, l1) mtPRF(lr, l1) ...
    %   scPRF(lr, l2) htPRF(lr, l2) ctPRF(lr, l2) mtPRF(lr, l2) ...
    %   ];
    
    srfData                 = [ ...
      scSRF(lr, l1) htSRF(lr, l1) ctSRF(lr, l1) mtSRF(lr, l1) ...
      scSRF(lr, l2) htSRF(lr, l2) ctSRF(lr, l2) mtSRF(lr, l2) ];
    
    srfSci                  = sciparts(reshape(srfData, [], 1)); 
    
    prfData                 = [ ...
      scPRF(lr, l1) htPRF(lr, l1) ctPRF(lr, l1) mtPRF(lr, l1) ...
      scPRF(lr, l2) htPRF(lr, l2) ctPRF(lr, l2) mtPRF(lr, l2) ];
    
    prfSci                  = sciparts(reshape(prfData, [], 1)); 
    

    outputText{m}           = strtrim(sprintf([ ...
      INT     TAB   STR     TAB   STR     TAB   STR     TAB ...
      DEC(1)  TAB   DEC(1)  TAB   DEC(3)  TAB   DEC(1)  TAB ... 
      SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ... 
      SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ... 
      SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ... 
      SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ], ...
      m, mCMPPath, mSRFPath, mPRFPath, ...
      mRTV, mCON, mRES, mfQ, ...
      srfSci, prfSci));
    
    catch err
      debugStamp();
      beep;
    end
    
  end
    
  sumHeading                =  {strtrim(sprintf('%s\t', ...
    'RTV',      'CON',      'RES',      'FQ',...
    'S-Avg',    'H-Avg',    'C-Avg',    'M-Avg', ...
    'S-Std',    'H-Std',    'C-Std',    'M-Std', ...
    'M.S-Avg',  'M.H-Avg',  'M.C-Avg',  'M.M-Avg', ...
    'M.S-Std',  'M.H-Std',  'M.C-Std',  'M.M-Std'))};
  
  outHeading                =  {strtrim(sprintf('%s\t', ...
    'ID',       'Patch',    'SRF',      'PRF', sumHeading{:}))};
  

  [outPath outExists]       = PatchSeriesProcessor.GetResourcePath('output', 'SummaryTable', 'csv');
  
  if forceOutputTable || ~outExists
    try
      cell2csv(outPath, vertcat( outHeading, outputText), '\n');
    catch err
      debugStamp();
      beep;
    end          
  end
  
  %   [sumPath sumExists]       = PatchSeriesProcessor.GetResourcePath('output', 'SummaryData', 'csv');
  %
  %   if forceOutput || ~sumExists
  %     try
  %       %sumHeading            = outHeading(:, [1 5:end]);
  %       sumText               = outputText(:, [1 5:end]);
  %       cell2csv(sumPath, vertcat( sumHeading, sumText), '\n');
  %     catch err
  %       debugStamp();
  %       beep;
  %     end
  %   end
  %
  
end

function [patchData] = processPatchData(varargin)
  
end

function [patchPlot] = composePatchPlots(imagePath, fQ, fQRow, P, plotColumns, W, H, varargin)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  persistent hFig
  
  %% Create Figure (or Reset)
  
  figWidth    = 600;
  figHeight   = H*figWidth/W;
  
  lineScale   = 1.5;
  
  
  if isempty(hFig) || ~ishandle(hFig)
    hFig        = figure('Visible', 'off', 'Position', [1 1 figWidth figHeight], 'Renderer', 'opengl'); %, 'color', 'none'); % 'HandleVisibility','callback', 
  else
    clf(hFig, 'reset');
    set(hFig, 'Visible', 'off', 'Position', [1 1 figWidth figHeight]);
  end
  
  axOpts        = {'Parent', hFig, 'color', 'none', 'Units', 'normalized', 'Position', [0 0 1 1], 'PlotBoxAspectRatio', [figWidth figHeight 1], 'Visible', 'off', 'clipping', 'on'};

  hAxes2        = axes(axOpts{:});
  hAxes         = axes('YScale', 'log', axOpts{:});
  
  % scSRF, htSRF, ctSRF, mtSRF
  plotColors    = {[0 1 1], [0 1 0], [1 0.75 0], [1 0 0], [0 0 0]};   %colors  = {'c', 'g', [1 0.75 0] , 'r', 'k'}; %
  lineOpts      = {'LineWidth', lineScale*0.85}; %, 'linesmoothing','on'};
  
  dataTable     = varargin;
  rowOffset     = 2-1;
  % E       = 0;
  fQRow         = fQRow-rowOffset-1;
  
  %% Process Data
  for n = 1:numel(dataTable)
    % E           = max(E, size(data{n}, 1));
    dataTable{n}     = dataTable{n}(rowOffset+1:end,:);
    % vMin(n)     = nanmin(dataTable{n}(:,P));
    % vMax(n)     = nanmax(dataTable{n}(:,P));
    % vMean(n)    = nanmean(dataTable{n}(:,P));
  end
  
  % fMin          = min(vMin);
  % fMax          = max(vMax);
  % fMean         = mean(vMean);
  
  hold(hAxes,   'on');
  hold(hAxes2,  'on');
  
  dMin1         = [];
  dMax1         = [];
  dMin2         = [];
  dMax2         = [];
  
  %% Plot Curves
  for n = [1 3:numel(dataTable)]
    DP2         = dataTable{n}(:,plotColumns(2));
    
    hP2         = plot(DP2, 'color', plotColors{n}, 'Parent', hAxes2, lineOpts{:}, 'linestyle', ':'); % , 'linewidth', R/3);
    
    dMin2       = min([dMin2 DP2(:)']);
    dMax2       = max([dMax2 DP2(:)']);
  end  
  
  DP2           = dataTable{2}(:,plotColumns(2));
  hP2           = plot(DP2, 'color', plotColors{2}, 'Parent', hAxes2, lineOpts{:}, 'linestyle', ':'); %, 'linewidth' , R);

  DF1           = 0; %data{1}(:,L(1))-max(data{1}(:,L(1)));
  
  %% Plot Curves
  for n = [1 3:numel(dataTable)]
    DP1         = dataTable{n}(:,plotColumns(1))-DF1;
    hP1         = plot(DP1, 'color', plotColors{n}, 'Parent', hAxes, lineOpts{:}); %, 'linewidth', R/3);
    
    dMin1       = min([dMin1 DP1(:)']);
    dMax1       = max([dMax1 DP1(:)']);
  end
  
  DP1 = dataTable{2}(:,plotColumns(1))-DF1;
  hP1 = plot(DP1, 'color', plotColors{2}, 'Parent', hAxes, lineOpts{:}); %, 'linewidth' , R);

  %% Fundamental Data
  vData   = [];
  for n = 1:numel(dataTable)
    vData = [vData reshape(dataTable{n}(  min(fQRow, size(dataTable{n},1)  ), plotColumns),[],1)];
  end
  
  [fR fE] = sciparts(reshape(vData, [], 1));
  
  fR      = reshape(fR, size(vData));
  fE      = reshape(fE, size(vData));
  
  %% Prepare formatter
  fNumF = '%1.2f\t';
  fStrF = '';
  for n = 1:size(fR,2)
    fStrF = [fStrF '\\color[rgb]{' num2str(plotColors{n}, ' %1.2f ') '}' fNumF];
  end
  
  %% Prepare Text
  fStr = {num2str(fQ,'%1.2f\n')};
  for n = 1:size(fR,1)
    %vStr      = num2str(fR(n, :),'%1.2f\t');
    vStr      = num2str(fR(n, :), fStrF);
    vStr      = [vStr sprintf('\te%+1d', fE(n,1))];
    fStr(n+1) = {vStr};
  end
  
  yl = get(hAxes, 'ylim');
  xl = get(hAxes, 'xlim');
  
    
  %% Fundamental Line %round(fQ)-D %round(fQ)-D
  line([fQRow fQRow]+1, yl, [0 0]-50, 'Color', [0.8 0 0], 'Parent', hAxes, lineOpts{:}, 'LineWidth', lineScale*2);  

  
  text(max(xl)*0.95, max(yl)*0.95, 0, fStr, 'Parent', hAxes, 'HorizontalAlignment', 'right', ...
    'VerticalAlignment', 'top', 'Color', 'g', 'FontSize', 8*lineScale, 'FontName', 'DIN-Bold', ... 
    'Interpreter', 'tex'); % 'FontWeight', 'bold',
  
  set(hAxes, 'XTickLabel', [], 'YTickLabel', [], 'ZTickLabel', [], 'XTick', [], 'YTick', [], 'ZTick', []);
  set(hAxes2, 'XTickLabel', [], 'YTickLabel', [], 'ZTickLabel', [], 'XTick', [], 'YTick', [], 'ZTick', []);
  
  if ischar(imagePath) && ~isempty(imagePath)   
    export_fig(imagePath, '-nocrop', '-a4', '-zbuffer', '-transparent', hFig); %'-native'
    patchPlot = imread(imagePath);
  else
    patchPlot = export_fig('nocrop', '-a2', '-opengl', '-transparent', hFig); %  '-native',
  end
  
  
end

function [patchComp W H] = composePatchImage(htID, scID, ctID, mtID)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  patchTypes              = {'Screen', 'Halftone', 'Contone', 'Monotone'};
  patchIDs                = {scID, htID, ctID, mtID};
  imageGroups             = {
    'Image', 'RetinaImage';
    'FFTImage', 'RetinaFFTImage'};
  
  compDim                 = 1;
  
  imageType               = cell(1, 2);
  img                     = cell(numel(patchTypes), numel(imageGroups), 2);
  
  imgSizes                = [];
  
  N                       = numel(patchTypes);
  P                       = size(imageGroups,2);
  Q                       = size(imageGroups,1);
  
  %% Load All Images
  for n = 1:N
    for p = 1:P
      for q = 1:Q
        img{n, p, q}      = PatchSeriesProcessor.LoadImage([patchTypes{n} ' ' imageGroups{p, q}], patchIDs{n});
        r                 = sub2ind([N P Q], n, p, q);
        imgSizes(r,:)     = size(img{n, p, q});
      end
    end
  end
  
  %% Crop All Images
  
  H                       = min(imgSizes(:,1));
  W                       = min(imgSizes(:,2));
  
  X1                      = 1+floor((imgSizes(:,2) - W) / 2);
  X2                      = X1+W-1;
  
  Y1                      = 1+floor((imgSizes(:,1) - H) / 2);
  Y2                      = Y1+H-1;
  
  for n = 1:N
    for p = 1:P
      for q = 1:Q
        r                 = sub2ind([N P Q], n, p, q);
        img{n, p, q}      = img{n, p, q}(Y1(r):Y2(r), X1(r):X2(r));
      end
    end
  end
  
  %% Composite Patch Image
  patchComp               = [];
  
  for n = 1:N
    groupComp             = [];
    for p = 1:P
      imageComp           = img{n, p, 1};
      S                   = size(imageComp, compDim);
      M                   = round(S/2):S;
      if compDim==2
        imageComp(:,M)    = img{n, p, 2}(:,M);
      else
        imageComp(M,:)    = img{n, p, 2}(M,:);
      end
      groupComp           = vertcat(groupComp, imageComp);
    end
    patchComp             = horzcat(patchComp, groupComp);
  end
  
  W = W * N;
  H = H * P;
  
end
