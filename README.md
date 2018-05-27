# Search Engine

Search Engine is an in-memory store of ids and keywords that lets you efficiently search for ids by keywords. There is no persistence of data between sessions and the store is empty upon startup.

## Installation

Clone this repo and run `sbt run`. You can also run `sbt assembly` and then use the resulting jar.

## Commands:

`help`: Show help text very similar to this document.

`exit`: Exit Search Engine.

`index <id> <keyword1> <keyword2> ...`: Store a document id (positive integer) and a list of keywords (alphanumeric). For example 'index 1 soup tomato cream salt'. This will replace any previous document with the same id.

`query <keyword>`: Query for ids of all documents with a given keyword. For example 'query tomato'.

`query <keyword1> & <keyword2>`: Query for ids of all documents with both keywords. For example 'query tomato & soup'.

`query <keyword1> | <keyword2>`: Query for ids of all documents with either keyword. For example 'query tomato | soup'.

`query expression1 operator expression2`: Queries with & and | can include nested & or | expressions. Nested queries must have parentheses but the overall query and literal keywords should not. For example 'query (butter | potato) & salt' or 'query (butter & potato) | (chicken & salt)'.