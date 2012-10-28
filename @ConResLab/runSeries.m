try
  if ~(exist('patchGenerator', 'var') && isscalar(patchGenerator)) ...
      || ~isa(patchGenerator, 'Grasppe.ConRes.PatchGenerator')
    ConResLab.openPatchGenerator;
  end
  
  % set(gcf,'Visible', 'off');
  
  commandwindow;
  
  r = Grasppe.Kit.ConRes.ResolutionRange; %[1.515	1.812	2.155	2.577	3.086]
  c = Grasppe.Kit.ConRes.ContrastRange;
  
  for t = 5:5:95  %Grasppe.Kit.ConRes.ToneRange,
    R=tic;
    patchGenerator.Processor.RunSeries(t, c, r);
    toc(R);
  end
end
