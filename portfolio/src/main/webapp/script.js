// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);
/**
 * Adds a random greeting to the page.
 */
function addRandomLanguage() {
  const languages =
      ['English', 'Swahili', 'French', 'Lingala (Spoken in the DR Congo)'];

  // Pick a random greeting.
  const language = languages[Math.floor(Math.random() * languages.length)];

  // Add it to the page.
  const languageContainer = document.getElementById('language-container');
  languageContainer.innerText = language;
}

function FetchFunction()
{
    console.log('fetching data from the servlet'); 

    const responsePromise = fetch('/data'); 
    responsePromise.then(handleResponse); 
}
function handleResponse(response)
{
    const textPromise = response.text(); 
    textPromise.then(addToDom);  
}
function addToDom(responsy)
{
    console.log('the response from the java servlet is: '+ responsy)
    const responseContainer = document.getElementById('response-container')
    responseContainer.innerText = responsy

}


function drawChart()
{
    console.log('in'); 
    const values = new google.visualization.DataTable();
    values.addColumn('string', 'Programming language'); 
    values.addColumn('number', 'Frequency'); 
    values.addRows([
        ['C++', 30], 
        ['Python',50], 
        ['Java',20]]); 

    const options = {
    'title': ' Daily Usage Frequency',
    'width':500,
    'height':400
  };
   console.log('before the rdv'); 
  const chart = new google.visualization.PieChart(
      document.getElementById('chart-container'));
  console.log('at the rdv'); 
  chart.draw(values, options);
}