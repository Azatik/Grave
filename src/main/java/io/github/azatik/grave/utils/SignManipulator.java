/*
 * This file is part of Grave, licensed under the MIT License (MIT).
 *
 * Copyright (c) Azatik <http://azatik.github.io>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.azatik.grave.utils;

import java.util.ArrayList;
import java.util.List;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

public class SignManipulator {  
    public ArrayList<String> getLines(TileEntity sign) {
        SignData signdata = sign.get(SignData.class).get();
        List<Text> textSignLines = signdata.getValue(Keys.SIGN_LINES).get().getAll();
        
        Texts.toPlain(textSignLines.get(0));
        
        ArrayList<String> stringSignLines = new ArrayList();
        stringSignLines.add(Texts.toPlain(textSignLines.get(0)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(1)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(2)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(3)));
        
        return stringSignLines;
}
    
    public ArrayList<String> getLines(SignData signdata) {
        List<Text> textSignLines = signdata.getValue(Keys.SIGN_LINES).get().getAll();

        Texts.toPlain(textSignLines.get(0));

        ArrayList<String> stringSignLines = new ArrayList();
        stringSignLines.add(Texts.toPlain(textSignLines.get(0)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(1)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(2)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(3)));

        return stringSignLines;
    }
    
    public ArrayList<String> getLines(ImmutableSignData signdata) {
        List<Text> textSignLines = signdata.getValue(Keys.SIGN_LINES).get().getAll();

        Texts.toPlain(textSignLines.get(0));

        ArrayList<String> stringSignLines = new ArrayList();
        stringSignLines.add(Texts.toPlain(textSignLines.get(0)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(1)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(2)));
        stringSignLines.add(Texts.toPlain(textSignLines.get(3)));

        return stringSignLines;
    }
    
    public boolean setLines(TileEntity entity, Text line0, Text line1, Text line2, Text line3) {
        SignData sign = entity.get(SignData.class).get();
        if (line0!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(0, line0));
        if (line1!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(1, line1));
        if (line2!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(2, line2));
        if (line3!=null) sign = sign.set(sign.getValue(Keys.SIGN_LINES).get().set(3, line3));
        entity.offer(sign);
        return true;
    }
}
