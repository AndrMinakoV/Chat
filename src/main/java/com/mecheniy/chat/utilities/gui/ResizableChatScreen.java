package com.mecheniy.chat.utilities.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ResizableChatScreen extends Screen {

    private EditBox chatInputField;
    private Button resizeButton;
    private int chatWidth;
    private int chatHeight;
    private boolean resizing;
    private int resizeStartX, resizeStartY;

    public ResizableChatScreen(Component title) {
        super(title);
        this.chatWidth = 200; // начальная ширина чата
        this.chatHeight = 100; // начальная высота чата
    }

// ...

    // В вашем методе init()
    @Override
    protected void init() {
        super.init();
        this.chatInputField = new EditBox(this.font, this.width / 2 - chatWidth / 2, this.height - 30, chatWidth, 20, Component.literal(""));
        this.addWidget(this.chatInputField);

        // Увеличиваем размер кнопки для изменения размера
        int buttonSize = 20; // Установите заметный размер для кнопки, например 20x20 пикселей
        this.resizeButton = new Button(this.width / 2 + chatWidth / 2 - buttonSize, this.height - chatHeight - buttonSize, buttonSize, buttonSize, Component.literal("↘"), button -> {
            // Логика обработки изменения размера окна чата
        });
        this.addWidget(this.resizeButton);
    }

// ...


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        this.chatInputField.render(poseStack, mouseX, mouseY, partialTicks);
        resizeButton.render(poseStack, mouseX, mouseY, partialTicks);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (resizeButton.isMouseOver(mouseX, mouseY)) {
            this.resizing = true;
            this.resizeStartX = (int) mouseX;
            this.resizeStartY = (int) mouseY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.resizing) {
            int newWidth = Math.max(this.chatWidth + (int) mouseX - resizeStartX, 100);
            int newHeight = Math.max(this.chatHeight + (int) mouseY - resizeStartY, 20);
            setChatSize(newWidth, newHeight);
            resizeStartX = (int) mouseX;
            resizeStartY = (int) mouseY;
            return true; // Событие обработано
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.resizing) {
            this.resizing = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void setChatSize(int width, int height) {
        this.chatWidth = width;
        this.chatHeight = height;
        this.chatInputField.setWidth(chatWidth);
        // Перемещаем кнопку изменения размера в соответствии с новыми размерами
        this.resizeButton.x = this.width / 2 + chatWidth / 2 - 8;
        this.resizeButton.y = this.height - chatHeight - 22;
    }
}
