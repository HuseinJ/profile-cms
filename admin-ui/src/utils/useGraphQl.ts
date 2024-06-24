export const useGraphql = async (query: String, variables: any) => {
    let response = await fetch('https://cms.hjusic.com/graphql', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            query,
            variables
        })
    });

    return await response.json();
}