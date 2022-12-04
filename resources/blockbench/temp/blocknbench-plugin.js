(function () {
    let button;
    Plugin.register('testplugin', {
        title: 'testpluguin',
        author: 'Manuel-3',
        description: 'Button that changes UV of a Per-face cube into Box-UV layout.',
        about: 'This plugin simply adds a button that rearranges your individual Per-face UV rectangles in the same way Box-UV mode would. This is handy if you want to use the standard Box-UV format, but also have the flexibility of Per-face UV mode.',
        icon: 'calendar_view_month',
        version: '0.0.1',
        variant: 'both',
        onload() {
            button = new Action('testplugin', {
                name: 'zrobto',
                description: 'Changes UV into Box-UV layout.',
                icon: 'calendar_view_month',
                click: function () {
                    Undo.initEdit({ elements: Cube.selected });
                    Cube.selected.forEach(cube => {

                        makeTop(cube)
                        makeBack(cube)
                        makeFront(cube)
                        UVEditor.selectAll()
                    });
                    Undo.finishEdit('convert to box-uv layout');
                    Canvas.updateView({ elements: Cube.selected, element_aspects: { uv: true } });
                }
            });
            cloneButton = new Action({
                id: 'clone-cube',
                name: 'Clone butt with offset',
                icon: 'accessibility',
                description: 'Generates a player shaped model',
                category: 'filter',
                click: function (ev) {
                    playerModelSettings.show()
                }
            })

            testbtn = new Action({
                id: 'testbtm',
                name: 'TEST',
                description: 'Generates a player shaped model',
                category: 'filter',
                click: function (ev) {
                    Cube.selected.forEach(cube => {
                        console.log(cube.from, cube.to)

                    });
                }
            })
            // Toolbars.uv_editor.add(cloneButton);
            Toolbars.uv_editor.add(button);

            Toolbars.main_tools.add(cloneButton)
            Toolbars.main_tools.add(testbtn);
        },
        onunload() {
            button.delete();
            cloneButton.delete();
            testbtn.delete();
        }
    });
})();



function makeTop(cube, offset) {
    if (offset == null) {
        offset = { x: 0, y: 0 }
    }

    const x = Math.round(cube.from[0]) + offset.x
    const y = Math.round(cube.from[2]) + offset.y
    const w = Math.round(cube.to[0])
    const h = Math.round(cube.to[2])

    console.log(cube)
    console.log([w, h, x, y], cube.name)
    cube.faces['down'].uv = [w, h, x, y];
    cube.faces['up'].uv = [w, h, x, y];
}

function makeBack(cube, offset) {
    if (offset == null) {
        offset = { x: 0, y: 0 }
    }

    const x = Math.round(cube.from[1]) + offset.x
    const y = Math.round(cube.from[0]) + offset.y
    const w = Math.round(cube.to[1])
    const h = Math.round(cube.to[0])

    console.log(cube)
    console.log([w, h, x, y], cube.name)
    cube.faces['east'].uv = [w, h, x, y];
    cube.faces['west'].uv = [w, h, x, y];
}


function makeFront(cube, offset) {

    if (offset == null) {
        offset = { x: 0, y: 0 }
    }

    const x = Math.round(cube.from[1]) + offset.x
    const y = Math.round(cube.from[2]) + offset.y
    const w = Math.round(cube.to[1])
    const h = Math.round(cube.to[2])

    console.log(cube)
    console.log([w, h, x, y], cube.name)
    cube.faces['south'].uv = [w, h, x, y];
    cube.faces['north'].uv = [w, h, x, y];
}



var playerModelSettings = new Dialog({
    title: 'Choose Model',
    id: 'playerModelSettings',
    form: {
        axis: { label: 'axis', type: 'select', options: { x: 'x', y: 'y', z: 'z' }, default: 'x' },
        amount: { label: 'amount', type: 'input', default: 1 },
        offset: { label: 'offset', type: 'input', default: 1 },
    }
});

var capeInfo = new Dialog({
    title: 'Cape Texture Warning',
    id: 'cape_warning',
    lines: [
        'You might have to edit your cape texture <br>to a square format(32x32) to make it work in-game!<br> <a class="open-in-browser" id="cape-gen-button" style="text-decoration: underline;">Click here</a> to import the texture template.',
        '<p></p>'
    ]
});


function cloneCubes(data) {
    Undo.initEdit({ element: Outliner.elements, outliner: true });

    let cubes = []
    Undo.initEdit({ elements: cubes });
    Cube.selected.forEach(cube => {
        let lastPoz1 = cube.from
        let lastPoz2 = cube.to
        const dir = [
            data.direction[0] * data.offset,
            data.direction[1] * data.offset,
            data.direction[2] * data.offset]

        steveGroup = new Group(cube.name + 's').init();
        for (let i = 0; i < data.amount; i++) {
            let newCube = new Cube({
                name: cube.name + '_' + i,
                from: lastPoz1,
                to: lastPoz2,
                autouv: 0,
                faces: {
                    north: { uv: [2, 2, 4, 4] },
                    south: { uv: [6, 2, 8, 4] },
                    west: { uv: [4, 2, 6, 4] },
                    east: { uv: [0, 2, 2, 4] },
                    up: { uv: [4, 2, 2, 0] },
                    down: { uv: [6, 0, 4, 2] }
                }
            }).addTo(steveGroup).init();

            lastPoz1 = [
                lastPoz1[0] + dir[0],
                lastPoz1[1] + dir[1],
                lastPoz1[2] + dir[2]]
            lastPoz2 = [
                lastPoz2[0] + dir[0],
                lastPoz2[1] + dir[1],
                lastPoz2[2] + dir[2]]

            cubes.push(newCube)
            console.log(lastPoz1, lastPoz2)
        }
    });
    playerModelSettings.hide();
    Undo.finishEdit('cloned thinkgs');
}

playerModelSettings.onConfirm = function (data) {
    dto =
    {
        direction: [0, 0, 0],
        offset: data.offset == '' ? 1 : parseFloat(data.offset),
        amount: data.amount == '' ? 1 : parseInt(data.amount)
    }
    console.log(data.axis)
    switch (data.axis) {
        case 'x':
            dto.direction[0] = 1
            break;
        case 'y':
            dto.direction[1] = 1
            break
        case 'z':
            dto.direction[2] = 1
            break;
    }
    console.log(dto)
    cloneCubes(dto)
    playerModelSettings.hide();
}

