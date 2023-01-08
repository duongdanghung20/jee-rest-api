import { useState, useEffect } from "react";

const useFetch = (url) => {
    const [data, setData] = useState(null);
    const [isPending, setIsPending] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(url, {
            mode: 'cors',
        })
        .then(res => {
            if (!res.ok) {
                console.log(res);
                throw Error("Fetch data failed.");
            }
            return res.json();
        }).then(data => {
            setData(data);
            setIsPending(false);
            setError(null);
        }).catch(err => {
            if (err.name === 'AbortError') {;}
            else {
                setError(err.message);
                setIsPending(false);
            }
        });
    }, [url])

    return { data, isPending, error };
}

export default useFetch;