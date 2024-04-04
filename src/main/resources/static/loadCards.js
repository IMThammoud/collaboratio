let fetchCards;
let serializeToJSON;
async function getCards(){
    fetchCards = await fetch("getCards");
    serializeToJSON =  await fetchCards.json();

    for (const serializeToJSONKey in serializeToJSON) {
        console.log(serializeToJSON[serializeToJSONKey])
        let newDiv = document.createElement("div");
        newDiv.className = "card";
        newDiv.style ="width: 15rem; margin: 20%;";

        let imgInside = document.createElement("img");
        imgInside.src = "https://picsum.photos/768";
        newDiv.appendChild(imgInside);

        let innerDiv = document.createElement("div");
        innerDiv.className = "card-body";

        // adding the childs to innerDiv
        let sessionTopic = document.createElement("h4");
        sessionTopic.className ="card-title";
        sessionTopic.innerText = serializeToJSON[serializeToJSONKey]["sessionTopic"];

        let hints = document.createElement("h5");
        hints.className = "card-subtitle";
        hints.innerText = serializeToJSON[serializeToJSONKey]["sessionHints"];

        let bitOfProblemText = document.createElement("p");
        bitOfProblemText.className = "card-text";
        bitOfProblemText.innerText = serializeToJSON[serializeToJSONKey]["sessionProblem"];

        let openSession = document.createElement("button");
        openSession.innerText = "Open Session";




        innerDiv.appendChild(sessionTopic);
        innerDiv.appendChild(hints);
        innerDiv.appendChild(bitOfProblemText);
        innerDiv.appendChild(openSession);

        newDiv.appendChild(innerDiv);

        let displayInline = document.createElement("div");
        displayInline.appendChild(newDiv);

        document.getElementById("ChangingContainer").appendChild(displayInline);


    }

    //console.log(serializeToJSON);
}

getCards();
