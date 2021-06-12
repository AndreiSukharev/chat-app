const init = () => {
  const iframeUrl = 'http://localhost:3000';
  const isOpened = document.querySelector(`iframe[src^="${iframeUrl}"]`) !== null;
  if (isOpened) return;

  const iframe = document.createElement('iframe');

  iframe.style.position = 'fixed';
  iframe.style.bottom = '20px';
  iframe.style.right = '20px';
  iframe.style.width = '100%';
  iframe.style.height = '100%';
  iframe.style.maxHeight = '600px';
  iframe.style.maxWidth = '400px';
  iframe.style.border = 0;
  iframe.style.zIndex = 99999;

  document.body.appendChild(iframe);
};

if (document.readyState === 'interactive' || document.readyState === 'complete') {
  init();
} else {
  document.addEventListener('DOMContentLoaded', init);
}