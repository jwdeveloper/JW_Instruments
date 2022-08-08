import json

f = open('chords.json')
data = json.load(f)

chords = data['chords']
result = []
for key, values in chords.items():
    for value in values:
        obj = {"key": value['key'], "suffix": value["suffix"], "fingers": [], "fret":0}
        obj['fingers'] = value["positions"][0]['fingers']
        obj['fret'] = value["positions"][0]['baseFret']
        result.append(obj)
        print(value)

json_string = json.dumps(result, indent=1)
with open("chords_output.json", "w") as outfile:
    outfile.write(json_string)