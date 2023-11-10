import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8080/timer/api",
    headers: {
        "Content-type": "application/json"
    }
});