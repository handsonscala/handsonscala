const puppeteer = require('puppeteer');
const [src, dest] = process.argv.slice(2)
puppeteer.launch().then(async function(browser){
  const page = await browser.newPage();
  await page.goto("file://" + src, {waitUntil: 'load'});
  await page.pdf({path: dest, format: 'A4'});
  process.exit(0)
})
